package com.github.gaboss44.expandlibreforge.features.advisor

import com.github.gaboss44.expandlibreforge.ExpandLibreforgePlugin
import com.github.gaboss44.expandlibreforge.features.advisor.core.MethodAdvisor
import net.bytebuddy.agent.builder.AgentBuilder
import net.bytebuddy.agent.builder.ResettableClassFileTransformer
import net.bytebuddy.asm.Advice
import net.bytebuddy.description.field.FieldDescription
import net.bytebuddy.description.method.MethodDescription
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.ClassFileLocator
import net.bytebuddy.dynamic.DynamicType
import net.bytebuddy.implementation.Implementation
import net.bytebuddy.implementation.bytecode.Removal
import net.bytebuddy.implementation.bytecode.StackManipulation
import net.bytebuddy.implementation.bytecode.member.FieldAccess
import net.bytebuddy.implementation.bytecode.member.MethodInvocation
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess
import net.bytebuddy.jar.asm.MethodVisitor
import net.bytebuddy.jar.asm.Opcodes
import net.bytebuddy.matcher.ElementMatchers
import net.bytebuddy.utility.JavaModule
import java.io.File
import java.lang.instrument.Instrumentation
import java.lang.reflect.Method
import java.util.jar.JarFile
import java.util.logging.Level
import kotlin.system.measureTimeMillis

class AdvisorInstaller(
    private val plugin: ExpandLibreforgePlugin,
    private val instrumentation: Instrumentation
) {

    private val listener = Listener(plugin)

    private val installationListener = InstallationListener(plugin)

    private val exceptionHandler = LoggingExceptionHandler(plugin)

    infix fun install(advisors: Iterable<MethodAdvisor<*>>) {
        val tt = measureTimeMillis {

            var builder: AgentBuilder = AgentBuilder.Default()
                .ignore(ElementMatchers.none())
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(AgentBuilder.TypeStrategy.Default.REDEFINE)
                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                .with(listener)
                .with(installationListener)

            val advisorsByClass = advisors.groupBy { it.targetClazz }

            for ((clazz, classAdvisors) in advisorsByClass) {
                builder = builder
                    .type(ElementMatchers.`is`(clazz))
                    .transform { bb, _, _, _, _ ->

                        var combined = bb

                        for (advisor in classAdvisors) {
                            combined = combined.visit(
                                Advice
                                    .to(advisor.adviceClazz)
                                    .withExceptionHandler(exceptionHandler)
                                    .on(advisor.methodMatcher)
                            )
                        }

                        combined
                    }
            }

            builder.installOn(instrumentation)

            try {
                val bridgeClass = Class.forName(
                    "com.github.gaboss44.expandlibreforge.agent.bootstrap.AgentBridge",
                    false,
                    ClassLoader.getSystemClassLoader() // bootstrap-visible
                )

                val reg = bridgeClass.getMethod("registerPluginClassLoader", ClassLoader::class.java)
                reg.invoke(null, plugin.javaClass.classLoader)

                plugin.logger.info("AgentBridge: Registered this plugin's classloader")

            } catch (ex: Throwable) {
                plugin.logger.log(Level.SEVERE, "AgentBridge: Could not register this plugin's classloader", ex)
            }
        }

        plugin.logger.info("ByteBuddy agent installed in ${tt / 1000}s (${tt}ms)")
    }

    private class Listener(private val plugin: ExpandLibreforgePlugin) : AgentBuilder.Listener {

        override fun onDiscovery(
            typeName: String,
            classLoader: ClassLoader?,
            module: JavaModule?,
            loaded: Boolean
        ) {
            /* NO OP */
        }

        override fun onIgnored(
            typeDescription: TypeDescription,
            classLoader: ClassLoader?,
            module: JavaModule?,
            loaded: Boolean
        ) {
            /* NO OP */
        }

        override fun onTransformation(
            typeDescription: TypeDescription,
            classLoader: ClassLoader?,
            module: JavaModule?,
            loaded: Boolean,
            dynamicType: DynamicType
        ) {
            this.plugin.logger.info("Transform applied to: ${typeDescription.name}")
        }

        override fun onError(
            typeName: String,
            classLoader: ClassLoader?,
            module: JavaModule?,
            loaded: Boolean,
            throwable: Throwable
        ) {
            this.plugin.logger.info("ERROR transforming $typeName: $throwable")
        }

        override fun onComplete(
            typeName: String,
            classLoader: ClassLoader?,
            module: JavaModule?,
            loaded: Boolean
        ) {
            /* NO OP */
        }
    }

    private class InstallationListener(
        private val plugin: ExpandLibreforgePlugin
    ) : AgentBuilder.InstallationListener {

        override fun onBeforeInstall(
            instrumentation: Instrumentation,
            classFileTransformer: ResettableClassFileTransformer
        ) {
            plugin.logger.info("ByteBuddy: before install: ${classFileTransformer.javaClass.name}")
        }

        override fun onInstall(
            instrumentation: Instrumentation,
            classFileTransformer: ResettableClassFileTransformer
        ) {
            plugin.logger.info("ByteBuddy: install: ${classFileTransformer.javaClass.name}")
        }

        override fun onError(
            instrumentation: Instrumentation,
            classFileTransformer: ResettableClassFileTransformer,
            throwable: Throwable
        ): Throwable? {
            plugin.logger.severe("ByteBuddy install error: ${throwable.message}")
            throwable.printStackTrace()
            return throwable
        }

        override fun onReset(
            instrumentation: Instrumentation,
            classFileTransformer: ResettableClassFileTransformer
        ) {
            plugin.logger.info("ByteBuddy: reset: ${classFileTransformer.javaClass.name}")
        }

        override fun onBeforeWarmUp(
            types: Set<Class<*>?>,
            classFileTransformer: ResettableClassFileTransformer
        ) {
            plugin.logger.info("ByteBuddy: before warm up (${types.size} types)")
        }

        override fun onWarmUpError(
            type: Class<*>,
            classFileTransformer: ResettableClassFileTransformer,
            throwable: Throwable
        ) {
            plugin.logger.severe("ByteBuddy warm up error in ${type.name}: ${throwable.message}")
            throwable.printStackTrace()
        }

        override fun onAfterWarmUp(
            types: Map<Class<*>?, ByteArray?>,
            classFileTransformer: ResettableClassFileTransformer,
            transformed: Boolean
        ) {
            plugin.logger.info("ByteBuddy: after warm up (${types.size} types), transformed=$transformed")
        }
    }

    private class LoggingExceptionHandler(
        private val plugin: ExpandLibreforgePlugin
    ) : Advice.ExceptionHandler {

        private val instanceField = FieldDescription.ForLoadedField(
            ExpandLibreforgePlugin::class.java
                .getDeclaredField("INSTANCE")
                .apply { isAccessible = true }
        )

        private val errorMethod = MethodDescription.ForLoadedMethod(
            ExpandLibreforgePlugin::class.java
                .getDeclaredMethod(
                    "logExceptionFromAdvice",
                    Throwable::class.java
                )
                .apply { isAccessible = true }
        )

        override fun resolve(
            instrumentedMethod: MethodDescription,
            instrumentedType: TypeDescription
        ): StackManipulation {

            return StackManipulation.Compound(
                FieldAccess.forField(instanceField).read(),
                MethodVariableAccess.REFERENCE.loadFrom(0),
                Swap,
                MethodInvocation.invoke(errorMethod)
            )
        }
    }

    private object Swap : StackManipulation {
        override fun isValid(): Boolean = true

        override fun apply(
            mv: MethodVisitor,
            ctx: Implementation.Context
        ): StackManipulation.Size {
            mv.visitInsn(Opcodes.SWAP)
            return StackManipulation.Size(0, 0)
        }
    }
}

package com.github.gaboss44.expandlibreforge.proxy.common.advisors

import com.github.gaboss44.expandlibreforge.features.advisor.core.MethodAdvisor
import com.github.gaboss44.expandlibreforge.features.advisor.nms.player.NmsPlayerAttackMethodAdvisor
import net.bytebuddy.description.method.MethodDescription
import net.bytebuddy.asm.Advice
import net.bytebuddy.matcher.ElementMatcher
import net.bytebuddy.matcher.ElementMatchers
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import org.bukkit.craftbukkit.entity.CraftEntity
import org.bukkit.craftbukkit.entity.CraftPlayer
import java.lang.reflect.Method
import java.util.function.Consumer

class NmsPlayerAttackMethodAdvisorImpl private constructor(
    val consumers: MutableList<Consumer<Context>> = mutableListOf()
) : NmsPlayerAttackMethodAdvisor<NmsPlayerAttackMethodAdvisorImpl.Context> {

    override val targetClazz = Player::class.java

    override val adviceClazz = NmsPlayerAttackAdvice::class.java

    override val methodMatcher: ElementMatcher<in MethodDescription> =
        ElementMatchers.`is`(
            Player::class.java.getDeclaredMethod(
                "attack",
                Entity::class.java
            )
        )

    override val contextFactory: MethodAdvisor.Context.Factory<Context> = ContextFactory

    override fun addConsumer(consumer: Consumer<Context>) {
        consumers.add(consumer)
    }

    class Arguments(
        val nmsPlayer: ServerPlayer,
        val nmsTarget: Entity
    ) : NmsPlayerAttackMethodAdvisor.Context.Arguments {

        override val instanceView = InstanceView(nmsPlayer)

        override val firstArgumentView = FirstArgumentView(nmsTarget)

        override fun getArgumentView(index: Int): NmsPlayerAttackMethodAdvisor.Context.ArgumentView? =
            when (index) {
                0 -> firstArgumentView
                else -> null
            }

        class FirstArgumentView(
            val target: Entity
        ) : NmsPlayerAttackMethodAdvisor.Context.FirstArgumentView {
            override val index: Int = 0
            override val bukkitEntity: CraftEntity get() = target.bukkitEntity
        }

        class InstanceView(
            val nmsPlayer: ServerPlayer
        ) : NmsPlayerAttackMethodAdvisor.Context.InstanceView {
            override val bukkitPlayer: CraftPlayer get() = nmsPlayer.bukkitEntity
        }
    }

    class Payload : NmsPlayerAttackMethodAdvisor.Context.Payload {
        override var shouldPerformAttack: Boolean = false
    }

    class Context(
        override val arguments: Arguments,
        override val payload: Payload,
        override val method: Method
    ) : NmsPlayerAttackMethodAdvisor.Context

    object ContextFactory : MethodAdvisor.Context.Factory<Context> {
        override fun create(
            rawArgs: Array<Any?>,
            instance: Any?,
            method: Method
        ): Context? {

            val nmsPlayer = instance as? ServerPlayer ?: return null
            val nmsTarget = rawArgs.getOrNull(0) as? Entity ?: return null

            return Context(
                arguments = Arguments(nmsPlayer, nmsTarget),
                payload = Payload(),
                method = method
            )
        }
    }

    companion object {
        private lateinit var instance: NmsPlayerAttackMethodAdvisorImpl

        fun getInstance(): NmsPlayerAttackMethodAdvisorImpl {
            if (!::instance.isInitialized) {
                instance = NmsPlayerAttackMethodAdvisorImpl()
            }
            return instance
        }

        fun getInstanceOrNull(): NmsPlayerAttackMethodAdvisorImpl? {
            return if (::instance.isInitialized) instance else null
        }
    }
}

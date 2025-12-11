package com.github.gaboss44.expandlibreforge.features.advisor.core

import net.bytebuddy.description.method.MethodDescription
import net.bytebuddy.matcher.ElementMatcher
import java.lang.reflect.Method
import java.util.function.Consumer

interface MethodAdvisor<Ctx: MethodAdvisor.Context> {

    val targetClazz: Class<*>

    val adviceClazz: Class<*>

    val methodMatcher: ElementMatcher<in MethodDescription>

    val contextFactory: Context.Factory<Ctx>

    fun addConsumer(consumer: Consumer<Ctx>)

    interface Context {

        val arguments: Arguments

        val payload: Payload

        val method: Method

        interface ArgumentView {

            val index: Int
        }

        interface InstanceView

        interface Arguments {

            fun getArgumentView(index: Int): ArgumentView?

            val instanceView: InstanceView?
        }

        interface Payload

        @FunctionalInterface
        fun interface Factory<Ctx: Context> {
            fun create(rawArgs: Array<Any?>, instance: Any?, method: Method): Ctx?
        }
    }
}

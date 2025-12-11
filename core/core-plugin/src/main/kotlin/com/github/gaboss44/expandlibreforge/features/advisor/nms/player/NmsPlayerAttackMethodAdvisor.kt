package com.github.gaboss44.expandlibreforge.features.advisor.nms.player

import com.github.gaboss44.expandlibreforge.features.advisor.core.MethodAdvisor
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.function.Consumer

interface NmsPlayerAttackMethodAdvisor<Ctx: NmsPlayerAttackMethodAdvisor.Context> : MethodAdvisor<Ctx> {

    override val contextFactory: MethodAdvisor.Context.Factory<Ctx>

    override fun addConsumer(consumer: Consumer<Ctx>)

    interface Context : MethodAdvisor.Context {

        override val arguments: Arguments

        override val payload: Payload

        interface ArgumentView : MethodAdvisor.Context.ArgumentView {
            override val index: Int
        }

        interface FirstArgumentView : ArgumentView {
            override val index get() = 0
            val bukkitEntity: Entity
        }

        interface InstanceView : MethodAdvisor.Context.InstanceView {
            val bukkitPlayer: Player
        }

        interface Arguments : MethodAdvisor.Context.Arguments {

            override fun getArgumentView(index: Int): ArgumentView?

            override val instanceView: InstanceView?

            val firstArgumentView: FirstArgumentView
        }

        interface Payload : MethodAdvisor.Context.Payload {
            var shouldPerformAttack: Boolean
        }
    }
}

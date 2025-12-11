package com.github.gaboss44.expandlibreforge.events.entity

import org.bukkit.entity.LivingEntity
import org.bukkit.event.HandlerList
import org.bukkit.event.entity.EntityEvent
import org.bukkit.inventory.ItemStack

class EntityAttemptSmashAttackEvent(
    override val attacker: LivingEntity,
    override val target: LivingEntity,
    weapon: ItemStack,
    override val originalResult: Boolean
) : EntityEvent(attacker), AttemptSmashAttackEvent {

    override val event get() = this

    override var result = Result.DEFAULT

    override val weapon = weapon
        get() = field.clone()

    override fun getEntity(): LivingEntity {
        return this.attacker
    }

    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}

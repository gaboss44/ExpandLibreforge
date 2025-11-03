package com.github.gaboss44.expandlibreforge.events.entity

import org.bukkit.entity.LivingEntity
import org.bukkit.event.HandlerList
import org.bukkit.event.entity.EntityEvent
import org.bukkit.inventory.ItemStack

class EntitySmashCheckEvent(
    val attacker: LivingEntity,
    val target: LivingEntity,
    val weapon: ItemStack,
    val originalResult: Boolean
) : EntityEvent(attacker) {

    var result = Result.DEFAULT

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

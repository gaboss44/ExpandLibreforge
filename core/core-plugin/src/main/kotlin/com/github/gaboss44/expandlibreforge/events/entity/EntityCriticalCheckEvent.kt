package com.github.gaboss44.expandlibreforge.events.entity

import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.HandlerList
import org.bukkit.event.entity.EntityEvent
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
class EntityCriticalCheckEvent(
    val attacker: LivingEntity,
    val target: Entity,
    val weapon: ItemStack,
    val source: DamageSource,
    var criticalMultiplier: Float,
    val critsDisabled: Boolean,
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

package com.github.gaboss44.expandlibreforge.events.entity

import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.HandlerList
import org.bukkit.event.entity.EntityEvent
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
class EntityAttemptCriticalAttackEvent(
    val attacker: LivingEntity,
    val target: Entity,
    weapon: ItemStack,
    val source: DamageSource,
    var criticalDamageMultiplier: Float,
    val criticalAttacksDisabled: Boolean,
    val originalResult: Boolean
) : EntityEvent(attacker) {

    var result = Result.DEFAULT

    var weapon = weapon
        get() = field.clone()
        private set

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

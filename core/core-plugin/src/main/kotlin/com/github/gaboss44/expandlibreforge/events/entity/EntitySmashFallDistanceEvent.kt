package com.github.gaboss44.expandlibreforge.events.entity

import com.github.gaboss44.expandlibreforge.util.scaledBy
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.HandlerList
import org.bukkit.event.entity.EntityEvent
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
class EntitySmashFallDistanceEvent(
    val attacker: LivingEntity,
    val target: Entity,
    val weapon: ItemStack,
    val source: DamageSource,
    val fallDistance: Float,
    var overrideFallDistance: Float = fallDistance,
    val ranges: List<Pair<Number, Number>>,
    var overrideRanges: List<Pair<Number, Number>> = ranges
) : EntityEvent(attacker) {

    override fun getEntity(): LivingEntity {
        return this.attacker
    }

    val finalFallBasedDamage get() = overrideFallDistance.scaledBy(overrideRanges)

    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}

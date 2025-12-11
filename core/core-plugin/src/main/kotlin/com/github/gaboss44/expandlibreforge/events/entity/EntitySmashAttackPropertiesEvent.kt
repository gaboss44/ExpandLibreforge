package com.github.gaboss44.expandlibreforge.events.entity

import com.github.gaboss44.expandlibreforge.events.DamageSourceAwareEvent
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.HandlerList
import org.bukkit.event.entity.EntityEvent
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
class EntitySmashAttackPropertiesEvent(
    val attacker: LivingEntity,
    val target: Entity,
    weapon: ItemStack,
    override val damageSource: DamageSource,
    var fallDistance: Float,
    val fallBasedDamageSteps: MutableList<Pair<Float, Float>>,
    fallBasedDamageFunction: (Float) -> Float
) : EntityEvent(attacker), DamageSourceAwareEvent {

    private var function = fallBasedDamageFunction

    val weapon = weapon
        get() = field.clone()

    var fallBasedDamageFunction
        get() = function
        set(value) {
            function = value
            functionChanged = true
        }

    var functionChanged = false
        private set

    val totalFallBasedDamage get() = function(fallDistance)

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

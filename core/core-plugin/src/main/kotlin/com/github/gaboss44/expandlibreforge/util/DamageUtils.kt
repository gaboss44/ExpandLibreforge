package com.github.gaboss44.expandlibreforge.util

import com.google.common.base.Function
import org.bukkit.event.entity.EntityDamageEvent
import java.util.EnumMap
import kotlin.math.max
import kotlin.math.min

@Suppress("DEPRECATION")
object DamageUtils {

    private val modifierFunctionsField = FieldUtils.getField(EntityDamageEvent::class.java, "modifierFunctions")

    @Suppress("UNCHECKED_CAST")
    fun getModifierFunctions(event: EntityDamageEvent) : EnumMap<EntityDamageEvent.DamageModifier, Function<in Double, Double>>? {
        val field = modifierFunctionsField?.get(event) ?: return null
        return try {
            field as EnumMap<EntityDamageEvent.DamageModifier, Function<in Double, Double>>
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun recalculateModifiers(
        event: EntityDamageEvent,
        changedModifier: EntityDamageEvent.DamageModifier,
        modifierFunctions: Map<EntityDamageEvent.DamageModifier, Function<in Double, Double>>
    ) {
        var remaining = 0.0
        var recalculate = false

        for (modifier in EntityDamageEvent.DamageModifier.entries) {
            if (!event.isApplicable(modifier)) {
                continue
            }
            if (modifier == changedModifier) {
                recalculate = true
            }
            if (modifier == EntityDamageEvent.DamageModifier.BASE || !recalculate) {
                remaining = max(remaining + event.getDamage(modifier), 0.0)
            } else modifierFunctions[modifier]?.let { function ->
                val newRemaining = function.apply(remaining)
                event.setDamage(modifier, min(newRemaining - remaining, 0.0))
                remaining = max(newRemaining, 0.0)
            }
        }
    }

    fun recalculateModifiers(
        event: EntityDamageEvent,
        modifierFunctions: Map<EntityDamageEvent.DamageModifier, Function<in Double, Double>>
    ) {
        var remaining = event.damage

        for (type in EntityDamageEvent.DamageModifier.entries) {
            if (!event.isApplicable(type) || type == EntityDamageEvent.DamageModifier.BASE) {
                continue
            }
            val damage = modifierFunctions[type]!!.apply(remaining)
            event.setDamage(type, damage)
            remaining += damage
        }
    }
}
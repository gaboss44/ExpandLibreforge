package com.github.gaboss44.expandlibreforge.features.multipliers

import org.bukkit.event.entity.EntityDamageEvent

object DamageMultipliers {

    private val delegate = mutableMapOf<EntityDamageEvent, MutableMap<String, Double>>()

    fun get(event: EntityDamageEvent, name: String) = delegate[event]?.get(name)

    fun getAll(event: EntityDamageEvent) = delegate[event]?.toMap()

    fun put(event: EntityDamageEvent, name: String, value: Double) {
        val multipliers = delegate.getOrPut(event) { mutableMapOf() }
        multipliers[name] = value
    }

    fun remove(event: EntityDamageEvent, name: String) {
        val multipliers = delegate[event] ?: return
        multipliers.remove(name)
        if (multipliers.isEmpty()) delegate.remove(event)
    }

    fun removeAll(event: EntityDamageEvent) {
        delegate.remove(event)
    }

    fun calculate(event: EntityDamageEvent): Double {
        val multipliers = delegate[event] ?: return 1.0
        var value = 1.0
        for (multiplier in multipliers.values) value *= multiplier
        return value
    }

    fun apply(event: EntityDamageEvent) {
        event.damage *= calculate(event)
    }

    fun consume(event: EntityDamageEvent) {
        apply(event)
        delegate.remove(event)
    }
}
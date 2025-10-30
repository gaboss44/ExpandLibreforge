package com.github.gaboss44.expandlibreforge.listeners

import com.github.gaboss44.expandlibreforge.features.multipliers.DamageMultipliers
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

object DamageListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onDamage(event: EntityDamageEvent) = DamageMultipliers.consume(event)
}
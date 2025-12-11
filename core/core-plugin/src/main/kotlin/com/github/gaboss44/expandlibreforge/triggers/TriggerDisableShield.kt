package com.github.gaboss44.expandlibreforge.triggers

import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import com.willfp.libreforge.triggers.tryAsLivingEntity
import io.papermc.paper.event.player.PlayerShieldDisableEvent
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

sealed class TriggerDisableShield(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.EVENT,
        TriggerParameter.VICTIM,
        TriggerParameter.PROJECTILE,
        TriggerParameter.VALUE
    )

    fun handle(event: PlayerShieldDisableEvent) {
        val victim = event.player
        val attacker = event.damager.tryAsLivingEntity()
        val projectile = event.damager as? Projectile

        if (attacker != null) {
            this.dispatch(
                attacker.toDispatcher(),
                TriggerData(
                    player = attacker as? Player,
                    victim = victim,
                    projectile = projectile,
                    event = event,
                    value = event.cooldown.toDouble()
                )
            )
        }
    }

    companion object {
        fun registerAllInto(category: Triggers) {
            category.register(HighestPriority)
            category.register(HighPriority)
            category.register(Default)
            category.register(NormalPriority)
            category.register(LowPriority)
            category.register(LowestPriority)
            category.register(Monitor)
        }
    }

    object HighestPriority : TriggerDisableShield("disable_shield_highest_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        fun onShieldDisable(event: PlayerShieldDisableEvent) = handle(event)
    }

    object HighPriority : TriggerDisableShield("disable_shield_high_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
        fun onShieldDisable(event: PlayerShieldDisableEvent) = handle(event)
    }

    object NormalPriority : TriggerDisableShield("disable_shield_normal_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
        fun onShieldDisable(event: PlayerShieldDisableEvent) = handle(event)
    }

    object LowPriority : TriggerDisableShield("disable_shield_low_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
        fun onShieldDisable(event: PlayerShieldDisableEvent) = handle(event)
    }

    object LowestPriority : TriggerDisableShield("disable_shield_lowest_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
        fun onShieldDisable(event: PlayerShieldDisableEvent) = handle(event)
    }

    object Monitor : TriggerDisableShield("disable_shield_monitor") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        fun onShieldDisable(event: PlayerShieldDisableEvent) = handle(event)
    }

    object Default : TriggerDisableShield("disable_shield") {
        @EventHandler(ignoreCancelled = true)
        fun onShieldDisable(event: PlayerShieldDisableEvent) = handle(event)
    }
}
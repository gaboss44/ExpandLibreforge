package com.github.gaboss44.expandlibreforge.triggers

import com.github.gaboss44.expandlibreforge.extensions.nonEmptyCurrentWeapon
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import com.willfp.libreforge.triggers.tryAsLivingEntity
import io.papermc.paper.event.player.PlayerShieldDisableEvent
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

sealed class TriggerShieldDisable(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.EVENT,
        TriggerParameter.VICTIM,
        TriggerParameter.ITEM,
        TriggerParameter.PROJECTILE,
        TriggerParameter.VALUE
    )

    fun handle(event: PlayerShieldDisableEvent) {
        val player = event.player

        val victim = event.damager.tryAsLivingEntity()

        val projectile = event.damager as? Projectile

        this.dispatch(
            player.toDispatcher(),
            TriggerData(
                player = player,
                victim = victim,
                item = victim?.nonEmptyCurrentWeapon,
                projectile = projectile,
                event = event,
                value = event.cooldown.toDouble()
            )
        )
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

    object HighestPriority : TriggerShieldDisable("shield_disable_highest_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        fun onShieldDisable(event: PlayerShieldDisableEvent) = handle(event)
    }

    object HighPriority : TriggerShieldDisable("shield_disable_high_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
        fun onShieldDisable(event: PlayerShieldDisableEvent) = handle(event)
    }

    object NormalPriority : TriggerShieldDisable("shield_disable_normal_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
        fun onShieldDisable(event: PlayerShieldDisableEvent) = handle(event)
    }

    object LowPriority : TriggerShieldDisable("shield_disable_low_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
        fun onShieldDisable(event: PlayerShieldDisableEvent) = handle(event)
    }

    object LowestPriority : TriggerShieldDisable("shield_disable_lowest_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
        fun onShieldDisable(event: PlayerShieldDisableEvent) = handle(event)
    }

    object Monitor : TriggerShieldDisable("shield_disable_monitor") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        fun onShieldDisable(event: PlayerShieldDisableEvent) = handle(event)
    }

    object Default : TriggerShieldDisable("shield_disable") {
        @EventHandler(ignoreCancelled = true)
        fun onShieldDisable(event: PlayerShieldDisableEvent) = handle(event)
    }
}

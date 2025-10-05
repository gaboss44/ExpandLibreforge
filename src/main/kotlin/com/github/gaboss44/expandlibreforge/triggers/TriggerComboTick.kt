package com.github.gaboss44.expandlibreforge.triggers

import com.github.gaboss44.expandlibreforge.events.combo.PlayerComboTickEvent
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

sealed class TriggerComboTick(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.EVENT,
        TriggerParameter.VALUE,
        TriggerParameter.ALT_VALUE
    )

    fun handle(event: PlayerComboTickEvent) {
        val player = event.player
        this.dispatch(
            player.toDispatcher(),
            TriggerData(
                player = player,
                event = event,
                value = event.combo.count.toDouble(),
                altValue = event.combo.score,
            )
        )
    }

    companion object {
        fun registerAllInto(category: Triggers) {
            category.register(Default)
            category.register(HighestPriority)
            category.register(HighPriority)
            category.register(NormalPriority)
            category.register(LowPriority)
            category.register(LowestPriority)
        }
    }

    object Default : TriggerComboTick("combo_tick") {
        @EventHandler(ignoreCancelled = true)
        fun onComboTick(event: PlayerComboTickEvent) = handle(event)
    }

    object HighestPriority : TriggerComboTick("combo_tick_highest_priority") {
        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        fun onComboTick(event: PlayerComboTickEvent) = handle(event)
    }

    object HighPriority : TriggerComboTick("combo_tick_high_priority") {
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        fun onComboTick(event: PlayerComboTickEvent) = handle(event)
    }

    object NormalPriority : TriggerComboTick("combo_tick_normal_priority") {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        fun onComboTick(event: PlayerComboTickEvent) = handle(event)
    }

    object LowPriority : TriggerComboTick("combo_tick_low_priority") {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        fun onComboTick(event: PlayerComboTickEvent) = handle(event)
    }

    object LowestPriority : TriggerComboTick("combo_tick_lowest_priority") {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        fun onComboTick(event: PlayerComboTickEvent) = handle(event)
    }
}
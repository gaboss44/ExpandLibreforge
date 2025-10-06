package com.github.gaboss44.expandlibreforge.triggers

import com.github.gaboss44.expandlibreforge.events.combo.PlayerComboEndEvent
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

sealed class TriggerComboEnd(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.EVENT,
        TriggerParameter.VALUE,
        TriggerParameter.ALT_VALUE
    )

    fun handle(event: PlayerComboEndEvent) {
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
            category.register(Monitor)
            category.register(HighestPriority)
            category.register(HighPriority)
            category.register(NormalPriority)
            category.register(LowPriority)
            category.register(LowestPriority)
        }
    }

    object Default : TriggerComboEnd("combo_end") {
        @EventHandler(ignoreCancelled = true)
        fun onComboEnd(event: PlayerComboEndEvent) = handle(event)
    }

    object Monitor : TriggerComboEnd("combo_end_monitor") {
        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        fun onComboEnd(event: PlayerComboEndEvent) = handle(event)
    }

    object HighestPriority : TriggerComboEnd("combo_end_highest_priority") {
        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        fun onComboEnd(event: PlayerComboEndEvent) = handle(event)
    }

    object HighPriority : TriggerComboEnd("combo_end_high_priority") {
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        fun onComboEnd(event: PlayerComboEndEvent) = handle(event)
    }

    object NormalPriority : TriggerComboEnd("combo_end_normal_priority") {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        fun onComboEnd(event: PlayerComboEndEvent) = handle(event)
    }

    object LowPriority : TriggerComboEnd("combo_end_low_priority") {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        fun onComboEnd(event: PlayerComboEndEvent) = handle(event)
    }

    object LowestPriority : TriggerComboEnd("combo_end_lowest_priority") {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        fun onComboEnd(event: PlayerComboEndEvent) = handle(event)
    }
}
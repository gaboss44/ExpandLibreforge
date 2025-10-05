package com.github.gaboss44.expandlibreforge.triggers

import com.github.gaboss44.expandlibreforge.events.combo.PlayerComboStartEvent
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

sealed class TriggerComboStart(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.EVENT,
        TriggerParameter.VALUE,
        TriggerParameter.ALT_VALUE
    )

    fun handle(event: PlayerComboStartEvent) {
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

    object Default : TriggerComboStart("combo_start") {
        @EventHandler(ignoreCancelled = true)
        fun onComboStart(event: PlayerComboStartEvent) = handle(event)
    }

    object HighestPriority : TriggerComboStart("combo_start_highest_priority") {
        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        fun onComboStart(event: PlayerComboStartEvent) = handle(event)
    }

    object HighPriority : TriggerComboStart("combo_start_high_priority") {
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        fun onComboStart(event: PlayerComboStartEvent) = handle(event)
    }

    object NormalPriority : TriggerComboStart("combo_start_normal_priority") {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        fun onComboStart(event: PlayerComboStartEvent) = handle(event)
    }

    object LowPriority : TriggerComboStart("combo_start_low_priority") {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        fun onComboStart(event: PlayerComboStartEvent) = handle(event)
    }

    object LowestPriority : TriggerComboStart("combo_start_lowest_priority") {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        fun onComboStart(event: PlayerComboStartEvent) = handle(event)
    }
}
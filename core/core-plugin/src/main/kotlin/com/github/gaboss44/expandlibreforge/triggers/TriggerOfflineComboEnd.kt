package com.github.gaboss44.expandlibreforge.triggers

import com.github.gaboss44.expandlibreforge.events.combo.OfflineComboEndEvent
import com.willfp.libreforge.GlobalDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

sealed class TriggerOfflineComboEnd(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.EVENT,
        TriggerParameter.VALUE,
        TriggerParameter.ALT_VALUE
    )

    fun handle(event: OfflineComboEndEvent) {
        this.dispatch(
            GlobalDispatcher,
            TriggerData(
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

    object Default : TriggerOfflineComboEnd("offline_combo_end") {
        @EventHandler(ignoreCancelled = true)
        fun onComboTick(event: OfflineComboEndEvent) = handle(event)
    }

    object HighestPriority : TriggerOfflineComboEnd("offline_combo_end_highest_priority") {
        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        fun onComboTick(event: OfflineComboEndEvent) = handle(event)
    }

    object HighPriority : TriggerOfflineComboEnd("offline_combo_end_high_priority") {
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        fun onComboTick(event: OfflineComboEndEvent) = handle(event)
    }

    object NormalPriority : TriggerOfflineComboEnd("offline_combo_end_normal_priority") {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        fun onComboTick(event: OfflineComboEndEvent) = handle(event)
    }

    object LowPriority : TriggerOfflineComboEnd("offline_combo_end_low_priority") {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        fun onComboTick(event: OfflineComboEndEvent) = handle(event)
    }

    object LowestPriority : TriggerOfflineComboEnd("offline_combo_end_lowest_priority") {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        fun onComboTick(event: OfflineComboEndEvent) = handle(event)
    }
}
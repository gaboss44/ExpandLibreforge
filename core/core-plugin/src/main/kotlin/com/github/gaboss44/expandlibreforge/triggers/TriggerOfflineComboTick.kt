package com.github.gaboss44.expandlibreforge.triggers

import com.github.gaboss44.expandlibreforge.events.combo.OfflineComboTickEvent
import com.willfp.libreforge.GlobalDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

sealed class TriggerOfflineComboTick(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.EVENT,
        TriggerParameter.VALUE,
        TriggerParameter.ALT_VALUE
    )

    fun handle(event: OfflineComboTickEvent) {
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

    object Default : TriggerOfflineComboTick("offline_combo_tick") {
        @EventHandler(ignoreCancelled = true)
        fun onComboTick(event: OfflineComboTickEvent) = handle(event)
    }

    object HighestPriority : TriggerOfflineComboTick("offline_combo_tick_highest_priority") {
        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        fun onComboTick(event: OfflineComboTickEvent) = handle(event)
    }

    object HighPriority : TriggerOfflineComboTick("offline_combo_tick_high_priority") {
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        fun onComboTick(event: OfflineComboTickEvent) = handle(event)
    }

    object NormalPriority : TriggerOfflineComboTick("offline_combo_tick_normal_priority") {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        fun onComboTick(event: OfflineComboTickEvent) = handle(event)
    }

    object LowPriority : TriggerOfflineComboTick("offline_combo_tick_low_priority") {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        fun onComboTick(event: OfflineComboTickEvent) = handle(event)
    }

    object LowestPriority : TriggerOfflineComboTick("offline_combo_tick_lowest_priority") {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        fun onComboTick(event: OfflineComboTickEvent) = handle(event)
    }
}
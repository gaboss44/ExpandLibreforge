package com.github.gaboss44.expandlibreforge.triggers

import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityExhaustionEvent

sealed class TriggerExhaustion(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.EVENT,
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM,
        TriggerParameter.VALUE,
        TriggerParameter.TEXT,
        TriggerParameter.VELOCITY
    )

    fun handle(event: EntityExhaustionEvent) {
        this.dispatch(
            event.entity.toDispatcher(),
            TriggerData(
                event = event,
                player = event.entity as? Player,
                victim = event.entity,
                value = event.exhaustion.toDouble(),
                text = event.exhaustionReason.name
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

    object Default : TriggerExhaustion("exhaustion") {
        @EventHandler(ignoreCancelled = true)
        fun onComboStart(event: EntityExhaustionEvent) = handle(event)
    }

    object Monitor : TriggerExhaustion("exhaustion_monitor") {
        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        fun onComboStart(event: EntityExhaustionEvent) = handle(event)
    }

    object HighestPriority : TriggerExhaustion("exhaustion_highest_priority") {
        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        fun onComboStart(event: EntityExhaustionEvent) = handle(event)
    }

    object HighPriority : TriggerExhaustion("exhaustion_high_priority") {
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        fun onComboStart(event: EntityExhaustionEvent) = handle(event)
    }

    object NormalPriority : TriggerExhaustion("exhaustion_normal_priority") {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        fun onComboStart(event: EntityExhaustionEvent) = handle(event)
    }

    object LowPriority : TriggerExhaustion("exhaustion_low_priority") {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        fun onComboStart(event: EntityExhaustionEvent) = handle(event)
    }

    object LowestPriority : TriggerExhaustion("exhaustion_lowest_priority") {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        fun onComboStart(event: EntityExhaustionEvent) = handle(event)
    }
}
package com.github.gaboss44.expandlibreforge.triggers

import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerExpChangeEvent

sealed class TriggerXpChange(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.EVENT,
        TriggerParameter.VALUE
    )

    fun handle(event: PlayerExpChangeEvent) {
        val player = event.player
        val value = event.amount
        this.dispatch(
            player.toDispatcher(),
            TriggerData(
                player = player,
                event = event,
                value = value.toDouble()
            )
        )
    }

    companion object {
        fun registerAllInto(category: Triggers) {
            category.register(LowestPriority)
            category.register(LowPriority)
            category.register(NormalPriority)
            category.register(HighPriority)
            category.register(HighestPriority)
            category.register(Monitor)
        }
    }

    object LowestPriority : TriggerXpChange("xp_change_lowest_priority") {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        fun onXpChange(event: PlayerExpChangeEvent) = handle(event)
    }

    object LowPriority : TriggerXpChange("xp_change_low_priority") {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        fun onXpChange(event: PlayerExpChangeEvent) = handle(event)
    }

    object NormalPriority : TriggerXpChange("xp_change_normal_priority") {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        fun onXpChange(event: PlayerExpChangeEvent) = handle(event)
    }

    object HighPriority : TriggerXpChange("xp_change_high_priority") {
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        fun onXpChange(event: PlayerExpChangeEvent) = handle(event)
    }

    object HighestPriority : TriggerXpChange("xp_change_highest_priority") {
        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        fun onXpChange(event: PlayerExpChangeEvent) = handle(event)
    }

    object Monitor : TriggerXpChange("xp_change_monitor") {
        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        fun onXpChange(event: PlayerExpChangeEvent) = handle(event)
    }
}

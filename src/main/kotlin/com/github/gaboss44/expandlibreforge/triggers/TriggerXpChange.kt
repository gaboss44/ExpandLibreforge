package com.github.gaboss44.expandlibreforge.triggers

import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.event.EventHandler
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

    object LowestPriority : TriggerXpChange("xp_change_lowest_priority") {
        @EventHandler(priority = org.bukkit.event.EventPriority.LOWEST, ignoreCancelled = true)
        fun onXpChange(event: PlayerExpChangeEvent) = handle(event)
    }

    object LowPriority : TriggerXpChange("xp_change_low_priority") {
        @EventHandler(priority = org.bukkit.event.EventPriority.LOW, ignoreCancelled = true)
        fun onXpChange(event: PlayerExpChangeEvent) = handle(event)
    }

    object NormalPriority : TriggerXpChange("xp_change_normal_priority") {
        @EventHandler(priority = org.bukkit.event.EventPriority.NORMAL, ignoreCancelled = true)
        fun onXpChange(event: PlayerExpChangeEvent) = handle(event)
    }

    object HighPriority : TriggerXpChange("xp_change_high_priority") {
        @EventHandler(priority = org.bukkit.event.EventPriority.HIGH, ignoreCancelled = true)
        fun onXpChange(event: PlayerExpChangeEvent) = handle(event)
    }

    object HighestPriority : TriggerXpChange("xp_change_highest_priority") {
        @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST, ignoreCancelled = true)
        fun onXpChange(event: PlayerExpChangeEvent) = handle(event)
    }
}
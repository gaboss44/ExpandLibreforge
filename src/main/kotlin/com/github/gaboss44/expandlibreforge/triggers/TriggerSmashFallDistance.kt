package com.github.gaboss44.expandlibreforge.triggers

import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import io.papermc.paper.event.entity.EntitySmashAttackFallDistanceEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

sealed class TriggerSmashFallDistance(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM,
        TriggerParameter.ITEM,
        TriggerParameter.EVENT,
        TriggerParameter.VALUE,
        TriggerParameter.ALT_VALUE,
        TriggerParameter.TEXT
    )

    fun handle(event: EntitySmashAttackFallDistanceEvent) {
        this.dispatch(
            event.entity.toDispatcher(),
            TriggerData(
                player = event.entity as? Player,
                victim = event.target,
                item = event.weapon,
                event = event,
                value = event.fallDistance,
                altValue = event.originalFallDistance,
                text = event.reason.name
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
            category.register(Monitor)
        }
    }

    object Default : TriggerSmashFallDistance("smash_fall_distance") {
        @EventHandler(ignoreCancelled = true)
        fun onDamage(event: EntitySmashAttackFallDistanceEvent) = handle(event)
    }

    object HighestPriority : TriggerSmashFallDistance("smash_fall_distance_highest_priority") {
        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        fun onDamage(event: EntitySmashAttackFallDistanceEvent) = handle(event)
    }

    object HighPriority : TriggerSmashFallDistance("smash_fall_distance_high_priority") {
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        fun onDamage(event: EntitySmashAttackFallDistanceEvent) = handle(event)
    }

    object NormalPriority : TriggerSmashFallDistance("smash_fall_distance_normal_priority") {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        fun onDamage(event: EntitySmashAttackFallDistanceEvent) = handle(event)
    }

    object LowPriority : TriggerSmashFallDistance("smash_fall_distance_low_priority") {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        fun onDamage(event: EntitySmashAttackFallDistanceEvent) = handle(event)
    }

    object LowestPriority : TriggerSmashFallDistance("smash_fall_distance_lowest_priority") {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        fun onDamage(event: EntitySmashAttackFallDistanceEvent) = handle(event)
    }

    object Monitor : TriggerSmashFallDistance("smash_fall_distance_monitor") {
        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        fun onDamage(event: EntitySmashAttackFallDistanceEvent) = handle(event)
    }
}
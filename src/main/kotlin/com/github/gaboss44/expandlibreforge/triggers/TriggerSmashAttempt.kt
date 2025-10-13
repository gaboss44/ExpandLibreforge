package com.github.gaboss44.expandlibreforge.triggers

import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import io.papermc.paper.event.entity.EntityAttemptSmashAttackEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

sealed class TriggerSmashAttempt(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM,
        TriggerParameter.ITEM,
        TriggerParameter.EVENT,
        TriggerParameter.VALUE,
        TriggerParameter.TEXT
    )

    fun handle(event: EntityAttemptSmashAttackEvent) {
        this.dispatch(
            event.entity.toDispatcher(),
            TriggerData(
                player = event.entity as? Player,
                victim = event.target,
                item = event.weapon,
                event = event,
                text = event.result.name
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

    object Default : TriggerSmashAttempt("smash_attempt") {
        @EventHandler(ignoreCancelled = true)
        fun onDamage(event: EntityAttemptSmashAttackEvent) = handle(event)
    }

    object HighestPriority : TriggerSmashAttempt("smash_attempt_highest_priority") {
        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        fun onDamage(event: EntityAttemptSmashAttackEvent) = handle(event)
    }

    object HighPriority : TriggerSmashAttempt("smash_attempt_high_priority") {
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        fun onDamage(event: EntityAttemptSmashAttackEvent) = handle(event)
    }

    object NormalPriority : TriggerSmashAttempt("smash_attempt_normal_priority") {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        fun onDamage(event: EntityAttemptSmashAttackEvent) = handle(event)
    }

    object LowPriority : TriggerSmashAttempt("smash_attempt_low_priority") {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        fun onDamage(event: EntityAttemptSmashAttackEvent) = handle(event)
    }

    object LowestPriority : TriggerSmashAttempt("smash_attempt_lowest_priority") {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        fun onDamage(event: EntityAttemptSmashAttackEvent) = handle(event)
    }

    object Monitor : TriggerSmashAttempt("smash_attempt_monitor") {
        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        fun onDamage(event: EntityAttemptSmashAttackEvent) = handle(event)
    }
}
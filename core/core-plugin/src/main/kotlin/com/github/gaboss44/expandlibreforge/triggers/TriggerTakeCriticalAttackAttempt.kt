package com.github.gaboss44.expandlibreforge.triggers

import com.github.gaboss44.expandlibreforge.events.entity.EntityAttemptCriticalAttackEvent
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

sealed class TriggerTakeCriticalAttackAttempt(id: String) : Trigger(id) {

    override val parameters = setOf(
        TriggerParameter.EVENT,
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM,
        TriggerParameter.ITEM,
        TriggerParameter.VALUE,
        TriggerParameter.TEXT
    )

    fun handle(event: EntityAttemptCriticalAttackEvent) {
        this.dispatch(
            event.target.toDispatcher(),
            TriggerData(
                event = event,
                player = event.target as? Player,
                victim = event.entity,
                item = event.weapon,
                value = event.criticalDamageMultiplier.toDouble(),
                text = event.result.name
            )
        )
    }

    companion object {
        fun registerAllInto(category: Triggers) {
            category.register(Default)
            category.register(LowestPriority)
            category.register(LowPriority)
            category.register(NormalPriority)
            category.register(HighPriority)
            category.register(HighestPriority)
            category.register(Monitor)
        }
    }

    object Default : TriggerTakeCriticalAttackAttempt("take_critical_attack_attempt") {
        @EventHandler(ignoreCancelled = true)
        fun onCriticalAttempt(event: EntityAttemptCriticalAttackEvent) = handle(event)
    }

    object LowestPriority : TriggerTakeCriticalAttackAttempt("take_critical_attack_attempt_lowest_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
        fun onCriticalAttempt(event: EntityAttemptCriticalAttackEvent) = handle(event)
    }

    object LowPriority : TriggerTakeCriticalAttackAttempt("take_critical_attack_attempt_low_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
        fun onCriticalAttempt(event: EntityAttemptCriticalAttackEvent) = handle(event)
    }

    object NormalPriority : TriggerTakeCriticalAttackAttempt("take_critical_attack_attempt_normal_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
        fun onCriticalAttempt(event: EntityAttemptCriticalAttackEvent) = handle(event)
    }

    object HighPriority : TriggerTakeCriticalAttackAttempt("take_critical_attack_attempt_high_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
        fun onCriticalAttempt(event: EntityAttemptCriticalAttackEvent) = handle(event)
    }

    object HighestPriority : TriggerTakeCriticalAttackAttempt("take_critical_attack_attempt_highest_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        fun onCriticalAttempt(event: EntityAttemptCriticalAttackEvent) = handle(event)
    }

    object Monitor : TriggerTakeCriticalAttackAttempt("take_critical_attack_attempt_monitor") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        fun onCriticalAttempt(event: EntityAttemptCriticalAttackEvent) = handle(event)
    }
}

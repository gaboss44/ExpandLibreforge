package com.github.gaboss44.expandlibreforge.triggers

import com.github.gaboss44.expandlibreforge.events.entity.AttemptSmashAttackEvent
import com.github.gaboss44.expandlibreforge.extensions.tryAsAttemptSmashAttackEvent
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityEvent

sealed class TriggerInflictSmashAttackAttempt(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM,
        TriggerParameter.ITEM,
        TriggerParameter.EVENT,
        TriggerParameter.VALUE,
        TriggerParameter.TEXT
    )

    fun handle(event: EntityEvent) {
        val event = event.tryAsAttemptSmashAttackEvent() ?: return
        this.handle0(event)
    }

    fun handle0(event: AttemptSmashAttackEvent) {
        this.dispatch(
            event.attacker.toDispatcher(),
            TriggerData(
                player = event.attacker as? Player,
                victim = event.target,
                item = event.weapon,
                event = event.event,
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

    object Default : TriggerInflictSmashAttackAttempt("inflict_smash_attack_attempt") {
        @EventHandler(ignoreCancelled = true)
        fun onSmashAttempt(event: EntityEvent) = handle(event)
    }

    object HighestPriority : TriggerInflictSmashAttackAttempt("inflict_smash_attack_attempt_highest_priority") {
        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        fun onSmashAttempt(event: EntityEvent) = handle(event)
    }

    object HighPriority : TriggerInflictSmashAttackAttempt("inflict_smash_attack_attempt_high_priority") {
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        fun onSmashAttempt(event: EntityEvent) = handle(event)
    }

    object NormalPriority : TriggerInflictSmashAttackAttempt("inflict_smash_attack_attempt_normal_priority") {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        fun onSmashAttempt(event: EntityEvent) = handle(event)
    }

    object LowPriority : TriggerInflictSmashAttackAttempt("inflict_smash_attack_attempt_low_priority") {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        fun onSmashAttempt(event: EntityEvent) = handle(event)
    }

    object LowestPriority : TriggerInflictSmashAttackAttempt("inflict_smash_attack_attempt_lowest_priority") {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        fun onSmashAttempt(event: EntityEvent) = handle(event)
    }

    object Monitor : TriggerInflictSmashAttackAttempt("inflict_smash_attack_attempt_monitor") {
        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        fun onSmashAttempt(event: EntityEvent) = handle(event)
    }
}

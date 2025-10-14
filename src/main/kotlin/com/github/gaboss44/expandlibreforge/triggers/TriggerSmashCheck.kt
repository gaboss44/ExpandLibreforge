package com.github.gaboss44.expandlibreforge.triggers

import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import io.papermc.paper.event.entity.EntityCanSmashAttackCheckEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

sealed class TriggerSmashCheck(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM,
        TriggerParameter.ITEM,
        TriggerParameter.EVENT,
        TriggerParameter.TEXT
    )

    fun handle(event: EntityCanSmashAttackCheckEvent) {
        this.dispatch(
            event.entity.toDispatcher(),
            TriggerData(
                player = event.entity as? Player,
                victim = event.entity,
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

    object Default : TriggerSmashCheck("smash_check") {
        @EventHandler(ignoreCancelled = true)
        fun onDamage(event: EntityCanSmashAttackCheckEvent) = handle(event)
    }

    object HighestPriority : TriggerSmashCheck("smash_check_highest_priority") {
        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        fun onDamage(event: EntityCanSmashAttackCheckEvent) = handle(event)
    }

    object HighPriority : TriggerSmashCheck("smash_check_high_priority") {
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        fun onDamage(event: EntityCanSmashAttackCheckEvent) = handle(event)
    }

    object NormalPriority : TriggerSmashCheck("smash_check_normal_priority") {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        fun onDamage(event: EntityCanSmashAttackCheckEvent) = handle(event)
    }

    object LowPriority : TriggerSmashCheck("smash_check_low_priority") {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        fun onDamage(event: EntityCanSmashAttackCheckEvent) = handle(event)
    }

    object LowestPriority : TriggerSmashCheck("smash_check_lowest_priority") {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        fun onDamage(event: EntityCanSmashAttackCheckEvent) = handle(event)
    }

    object Monitor : TriggerSmashCheck("smash_check_monitor") {
        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        fun onDamage(event: EntityCanSmashAttackCheckEvent) = handle(event)
    }
}
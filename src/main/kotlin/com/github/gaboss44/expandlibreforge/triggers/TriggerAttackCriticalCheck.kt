package com.github.gaboss44.expandlibreforge.triggers

import com.willfp.eco.util.toNiceString
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import io.papermc.paper.event.player.PlayerAttackEntityCriticalCheckEvent
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

sealed class TriggerAttackCriticalCheck(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.EVENT,
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM,
        TriggerParameter.ITEM,
        TriggerParameter.VELOCITY,
        TriggerParameter.VALUE,
        TriggerParameter.ALT_VALUE,
        TriggerParameter.TEXT
    )

    fun handle(event: PlayerAttackEntityCriticalCheckEvent) {
        this.dispatch(
            event.player.toDispatcher(),
            TriggerData(
                event = event,
                player = event.player,
                item = event.weapon,
                velocity = event.player.velocity,
                victim = event.target as? LivingEntity,
                value = event.baseDamage.toDouble(),
                altValue = event.enchantedDamage.toDouble(),
                text = event.criticalMultiplier.toNiceString()
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

    object Default : TriggerAttackCriticalCheck("attack_critical_check") {
        @EventHandler(ignoreCancelled = true)
        fun onAttackCriticalCheck(event: PlayerAttackEntityCriticalCheckEvent) = handle(event)
    }

    object HighestPriority : TriggerAttackCriticalCheck("attack_critical_check_highest_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        fun onAttackCriticalCheck(event: PlayerAttackEntityCriticalCheckEvent) = handle(event)
    }

    object HighPriority : TriggerAttackCriticalCheck("attack_critical_check_high_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
        fun onAttackCriticalCheck(event: PlayerAttackEntityCriticalCheckEvent) = handle(event)
    }

    object NormalPriority : TriggerAttackCriticalCheck("attack_critical_check_normal_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
        fun onAttackCriticalCheck(event: PlayerAttackEntityCriticalCheckEvent) = handle(event)
    }

    object LowPriority : TriggerAttackCriticalCheck("attack_critical_check_low_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
        fun onAttackCriticalCheck(event: PlayerAttackEntityCriticalCheckEvent) = handle(event)
    }

    object LowestPriority : TriggerAttackCriticalCheck("attack_critical_check_lowest_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
        fun onAttackCriticalCheck(event: PlayerAttackEntityCriticalCheckEvent) = handle(event)
    }

    object Monitor : TriggerAttackCriticalCheck("attack_critical_check_monitor") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        fun onAttackCriticalCheck(event: PlayerAttackEntityCriticalCheckEvent) = handle(event)
    }
}
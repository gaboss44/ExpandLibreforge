package com.github.gaboss44.expandlibreforge.triggers

import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler

sealed class TriggerPreAttack(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.EVENT,
        TriggerParameter.VICTIM,
        TriggerParameter.LOCATION,
        TriggerParameter.VELOCITY
    )

    fun handle(event: PrePlayerAttackEntityEvent) {
        this.dispatch(
            event.player.toDispatcher(),
            TriggerData(
                player = event.player,
                event = event,
                victim = event.attacked as? LivingEntity,
                location = event.player.location,
                velocity = event.player.velocity,
            )
        )
    }

    companion object {
        fun registerAllInto(category: Triggers) {
            category.register(Monitor)
            category.register(LowestPriority)
            category.register(LowPriority)
            category.register(NormalPriority)
            category.register(HighPriority)
            category.register(HighestPriority)
        }
    }

    object Default : TriggerPreAttack("pre_attack") {
        @EventHandler(ignoreCancelled = true)
        fun onXpChange(event: PrePlayerAttackEntityEvent) = handle(event)
    }

    object Monitor : TriggerPreAttack("pre_attack_monitor") {
        @EventHandler(priority = org.bukkit.event.EventPriority.MONITOR, ignoreCancelled = true)
        fun onXpChange(event: PrePlayerAttackEntityEvent) = handle(event)
    }

    object LowestPriority : TriggerPreAttack("pre_attack_lowest_priority") {
        @EventHandler(priority = org.bukkit.event.EventPriority.LOWEST, ignoreCancelled = true)
        fun onXpChange(event: PrePlayerAttackEntityEvent) = handle(event)
    }

    object LowPriority : TriggerPreAttack("pre_attack_low_priority") {
        @EventHandler(priority = org.bukkit.event.EventPriority.LOW, ignoreCancelled = true)
        fun onXpChange(event: PrePlayerAttackEntityEvent) = handle(event)
    }

    object NormalPriority : TriggerPreAttack("pre_attack_normal_priority") {
        @EventHandler(priority = org.bukkit.event.EventPriority.NORMAL, ignoreCancelled = true)
        fun onXpChange(event: PrePlayerAttackEntityEvent) = handle(event)
    }

    object HighPriority : TriggerPreAttack("pre_attack_high_priority") {
        @EventHandler(priority = org.bukkit.event.EventPriority.HIGH, ignoreCancelled = true)
        fun onXpChange(event: PrePlayerAttackEntityEvent) = handle(event)
    }

    object HighestPriority : TriggerPreAttack("pre_attack_highest_priority") {
        @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST, ignoreCancelled = true)
        fun onXpChange(event: PrePlayerAttackEntityEvent) = handle(event)
    }
}

package com.github.gaboss44.expandlibreforge.triggers

import com.github.gaboss44.expandlibreforge.extensions.nonEmptyCurrentWeapon
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler

sealed class TriggerPrePlayerAttack(id: String) : Trigger(id) {
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
                item = event.player.nonEmptyCurrentWeapon,
                location = event.player.location,
                velocity = event.player.velocity,
            )
        )
    }

    companion object {
        fun registerAllInto(category: Triggers) {
            category.register(Default)
            category.register(Monitor)
            category.register(LowestPriority)
            category.register(LowPriority)
            category.register(NormalPriority)
            category.register(HighPriority)
            category.register(HighestPriority)
        }
    }

    object Default : TriggerPrePlayerAttack("pre_player_attack") {
        @EventHandler(ignoreCancelled = true)
        fun onXpChange(event: PrePlayerAttackEntityEvent) = handle(event)
    }

    object Monitor : TriggerPrePlayerAttack("pre_player_attack_monitor") {
        @EventHandler(priority = org.bukkit.event.EventPriority.MONITOR, ignoreCancelled = true)
        fun onXpChange(event: PrePlayerAttackEntityEvent) = handle(event)
    }

    object LowestPriority : TriggerPrePlayerAttack("pre_player_attack_lowest_priority") {
        @EventHandler(priority = org.bukkit.event.EventPriority.LOWEST, ignoreCancelled = true)
        fun onXpChange(event: PrePlayerAttackEntityEvent) = handle(event)
    }

    object LowPriority : TriggerPrePlayerAttack("pre_player_attack_low_priority") {
        @EventHandler(priority = org.bukkit.event.EventPriority.LOW, ignoreCancelled = true)
        fun onXpChange(event: PrePlayerAttackEntityEvent) = handle(event)
    }

    object NormalPriority : TriggerPrePlayerAttack("pre_player_attack_normal_priority") {
        @EventHandler(priority = org.bukkit.event.EventPriority.NORMAL, ignoreCancelled = true)
        fun onXpChange(event: PrePlayerAttackEntityEvent) = handle(event)
    }

    object HighPriority : TriggerPrePlayerAttack("pre_player_attack_high_priority") {
        @EventHandler(priority = org.bukkit.event.EventPriority.HIGH, ignoreCancelled = true)
        fun onXpChange(event: PrePlayerAttackEntityEvent) = handle(event)
    }

    object HighestPriority : TriggerPrePlayerAttack("pre_player_attack_highest_priority") {
        @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST, ignoreCancelled = true)
        fun onXpChange(event: PrePlayerAttackEntityEvent) = handle(event)
    }
}

package com.github.gaboss44.expandlibreforge.triggers

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
import com.github.gaboss44.expandlibreforge.util.tryDamagerAsLivingEntity
import com.github.gaboss44.expandlibreforge.util.tryDamagerAsProjectile
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import com.willfp.libreforge.triggers.tryAsLivingEntity
import io.lumine.mythic.bukkit.MythicBukkit
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

sealed class TriggerInflictKnockback(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM,
        TriggerParameter.PROJECTILE,
        TriggerParameter.EVENT,
        TriggerParameter.VALUE,
        TriggerParameter.VELOCITY
    )

    fun handle(event: EntityKnockbackByEntityEvent) {
        val damager = event.tryDamagerAsLivingEntity() ?: return

        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            val victim = event.entity.tryAsLivingEntity()
            if (MythicBukkit.inst().mobManager.isMythicMob(victim)) {
                return
            }
        }

        val victim = event.entity.tryAsLivingEntity()
        val projectile = event.tryDamagerAsProjectile()
        
        val vector = event.knockback

        this.dispatch(
            damager.toDispatcher(),
            TriggerData(
                player = damager as? Player,
                victim = victim,
                event = event,
                projectile = projectile,
                value = vector.length(),
                velocity = vector
            )
        )
    }

    companion object {
        fun registerAllInto(category: Triggers) {
            category.register(HighestPriority)
            category.register(HighPriority)
            category.register(Default)
            category.register(NormalPriority)
            category.register(LowPriority)
            category.register(LowestPriority)
            category.register(Monitor)
        }
    }

    object HighestPriority : TriggerInflictKnockback("inflict_knockback_highest_priority") {
        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        fun onDamage(event: EntityKnockbackByEntityEvent) = handle(event)
    }

    object HighPriority : TriggerInflictKnockback("inflict_knockback_high_priority") {
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        fun onDamage(event: EntityKnockbackByEntityEvent) = handle(event)
    }

    object NormalPriority : TriggerInflictKnockback("inflict_knockback_normal_priority") {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        fun onDamage(event: EntityKnockbackByEntityEvent) = handle(event)
    }

    object LowPriority : TriggerInflictKnockback("inflict_knockback_low_priority") {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        fun onDamage(event: EntityKnockbackByEntityEvent) = handle(event)
    }

    object LowestPriority : TriggerInflictKnockback("inflict_knockback_lowest_priority") {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        fun onDamage(event: EntityKnockbackByEntityEvent) = handle(event)
    }

    object Monitor : TriggerInflictKnockback("inflict_knockback_monitor") {
        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        fun onDamage(event: EntityKnockbackByEntityEvent) = handle(event)
    }

    object Default : TriggerInflictKnockback("inflict_knockback") {
        @EventHandler(ignoreCancelled = true)
        fun onDamage(event: EntityKnockbackByEntityEvent) = handle(event)
    }
}
package com.github.gaboss44.expandlibreforge.triggers

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
import com.github.gaboss44.expandlibreforge.extensions.nonEmptyCurrentWeapon
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import com.willfp.libreforge.triggers.tryAsLivingEntity
import io.lumine.mythic.bukkit.MythicBukkit
import io.papermc.paper.event.entity.EntityKnockbackEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

sealed class TriggerTakeKnockback(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM,
        TriggerParameter.ITEM,
        TriggerParameter.PROJECTILE,
        TriggerParameter.EVENT,
        TriggerParameter.VALUE,
        TriggerParameter.ALT_VALUE,
        TriggerParameter.VELOCITY
    )

    fun handle(event: EntityKnockbackEvent) {

        val entity = event.entity

        val byEntityEvent = event as? EntityKnockbackByEntityEvent

        val attacker = byEntityEvent?.hitBy?.tryAsLivingEntity()

        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            if (attacker != null && MythicBukkit.inst().mobManager.isMythicMob(attacker)) {
                return
            }
        }

        val projectile = byEntityEvent?.hitBy as? Projectile

        val vector = event.knockback
        val length = vector.length()
        val strength = byEntityEvent?.knockbackStrength

        this.dispatch(
            entity.toDispatcher(),
            TriggerData(
                player = entity as? Player,
                victim = attacker,
                item = attacker?.nonEmptyCurrentWeapon,
                event = event,
                projectile = projectile,
                value = length,
                altValue = strength?.toDouble() ?: length,
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

    object HighestPriority : TriggerTakeKnockback("take_knockback_highest_priority") {
        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        fun onDamage(event: EntityKnockbackEvent) = handle(event)
    }

    object HighPriority : TriggerTakeKnockback("take_knockback_high_priority") {
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        fun onDamage(event: EntityKnockbackEvent) = handle(event)
    }

    object NormalPriority : TriggerTakeKnockback("take_knockback_normal_priority") {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        fun onDamage(event: EntityKnockbackEvent) = handle(event)
    }

    object LowPriority : TriggerTakeKnockback("take_knockback_low_priority") {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        fun onDamage(event: EntityKnockbackEvent) = handle(event)
    }

    object LowestPriority : TriggerTakeKnockback("take_knockback_lowest_priority") {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        fun onDamage(event: EntityKnockbackEvent) = handle(event)
    }

    object Monitor : TriggerTakeKnockback("take_knockback_monitor") {
        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        fun onDamage(event: EntityKnockbackEvent) = handle(event)
    }

    object Default : TriggerTakeKnockback("take_knockback") {
        @EventHandler(ignoreCancelled = true)
        fun onDamage(event: EntityKnockbackEvent) = handle(event)
    }
}
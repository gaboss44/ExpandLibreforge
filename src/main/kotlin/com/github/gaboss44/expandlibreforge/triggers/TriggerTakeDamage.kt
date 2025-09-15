package com.github.gaboss44.expandlibreforge.triggers

import com.github.gaboss44.expandlibreforge.util.tryDamagerAsLivingEntity
import com.github.gaboss44.expandlibreforge.util.tryDamagerAsProjectile
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.tryAsLivingEntity
import io.lumine.mythic.bukkit.MythicBukkit
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

sealed class TriggerTakeDamage(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM,
        TriggerParameter.PROJECTILE,
        TriggerParameter.EVENT,
        TriggerParameter.VALUE
    )

    fun handle(event: EntityDamageEvent) {

        val entity = event.entity

        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            if (event is EntityDamageByEntityEvent) {
                val attacker = event.damager.tryAsLivingEntity()
                if (MythicBukkit.inst().mobManager.isMythicMob(attacker)) {
                    return
                }
            }
        }

        val byEntityEvent = event as? EntityDamageByEntityEvent

        val victim = byEntityEvent?.tryDamagerAsLivingEntity()

        val projectile = byEntityEvent?.tryDamagerAsProjectile()

        this.dispatch(
            entity.toDispatcher(),
            TriggerData(
                player = entity as? Player,
                victim = victim,
                event = event,
                projectile = projectile,
                value = event.finalDamage,
                altValue = event.damage
            )
        )
    }

    object HighestPriority : TriggerTakeDamage("take_damage_highest_priority") {
        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        fun onDamage(event: EntityDamageEvent) = handle(event)
    }

    object HighPriority : TriggerTakeDamage("take_damage_high_priority") {
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        fun onDamage(event: EntityDamageEvent) = handle(event)
    }

    object NormalPriority : TriggerTakeDamage("take_damage_normal_priority") {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        fun onDamage(event: EntityDamageEvent) = handle(event)
    }

    object LowPriority : TriggerTakeDamage("take_damage_low_priority") {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        fun onDamage(event: EntityDamageEvent) = handle(event)
    }

    object LowestPriority : TriggerTakeDamage("take_damage_lowest_priority") {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        fun onDamage(event: EntityDamageEvent) = handle(event)
    }
}
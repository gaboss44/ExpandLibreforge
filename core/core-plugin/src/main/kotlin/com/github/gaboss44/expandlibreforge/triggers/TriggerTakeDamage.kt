package com.github.gaboss44.expandlibreforge.triggers

import com.github.gaboss44.expandlibreforge.extensions.nonEmptyCurrentWeapon
import com.github.gaboss44.expandlibreforge.features.multipliers.DamageMultipliers
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import com.willfp.libreforge.triggers.tryAsLivingEntity
import io.lumine.mythic.bukkit.MythicBukkit
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

@Suppress("UnstableApiUsage")
sealed class TriggerTakeDamage(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM,
        TriggerParameter.ITEM,
        TriggerParameter.PROJECTILE,
        TriggerParameter.EVENT,
        TriggerParameter.VALUE,
        TriggerParameter.VELOCITY
    )

    fun handle(event: EntityDamageEvent) {
        val source = event.damageSource
        val entity = event.entity

        val byEntityEvent = event as? EntityDamageByEntityEvent

        val attacker =
            byEntityEvent?.damager?.tryAsLivingEntity() ?:
            source.causingEntity as? LivingEntity ?:
            source.directEntity as? LivingEntity

        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            if (event is EntityDamageByEntityEvent) {
                if (attacker != null && MythicBukkit.inst().mobManager.isMythicMob(attacker)) {
                    return
                }
            }
        }

        val projectile =
            byEntityEvent?.damager as? Projectile ?:
            source.directEntity as? Projectile

        this.dispatch(
            entity.toDispatcher(),
            TriggerData(
                player = entity as? Player,
                victim = attacker,
                item = attacker?.nonEmptyCurrentWeapon,
                event = event,
                projectile = projectile,
                value = event.finalDamage * DamageMultipliers.calculate(event),
                altValue = event.damage
            )
        )
    }

    companion object {
        fun registerAllInto(category: Triggers) {
            category.register(HighestPriority)
            category.register(HighPriority)
            category.register(NormalPriority)
            category.register(LowPriority)
            category.register(LowestPriority)
            category.register(Monitor)
        }
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

    object Monitor : TriggerTakeDamage("take_damage_monitor") {
        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        fun onDamage(event: EntityDamageEvent) = handle(event)
    }
}
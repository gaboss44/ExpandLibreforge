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

@Suppress("UnstableApiUsage")
sealed class TriggerInflictDamage(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM,
        TriggerParameter.ITEM,
        TriggerParameter.PROJECTILE,
        TriggerParameter.EVENT,
        TriggerParameter.VALUE,
        TriggerParameter.ALT_VALUE
    )

    fun handle(event: EntityDamageByEntityEvent) {
        val source = event.damageSource

        val attacker =
            event.damager.tryAsLivingEntity() ?:
            source.causingEntity as? LivingEntity ?:
            source.directEntity as? LivingEntity ?:
            return

        val victim = event.entity

        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            if (MythicBukkit.inst().mobManager.isMythicMob(victim)) {
                return
            }
        }

        val projectile =
            event.damager as? Projectile ?:
            source.directEntity as? Projectile

        this.dispatch(
            attacker.toDispatcher(),
            TriggerData(
                player = attacker as? Player,
                victim = victim as? LivingEntity,
                item = attacker.nonEmptyCurrentWeapon,
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

    object HighestPriority : TriggerInflictDamage("inflict_damage_highest_priority") {
        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        fun onDamage(event: EntityDamageByEntityEvent) = handle(event)
    }

    object HighPriority : TriggerInflictDamage("inflict_damage_high_priority") {
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        fun onDamage(event: EntityDamageByEntityEvent) = handle(event)
    }

    object NormalPriority : TriggerInflictDamage("inflict_damage_normal_priority") {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        fun onDamage(event: EntityDamageByEntityEvent) = handle(event)
    }

    object LowPriority : TriggerInflictDamage("inflict_damage_low_priority") {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        fun onDamage(event: EntityDamageByEntityEvent) = handle(event)
    }

    object LowestPriority : TriggerInflictDamage("inflict_damage_lowest_priority") {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        fun onDamage(event: EntityDamageByEntityEvent) = handle(event)
    }

    object Monitor : TriggerInflictDamage("inflict_damage_monitor") {
        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        fun onDamage(event: EntityDamageByEntityEvent) = handle(event)
    }
}
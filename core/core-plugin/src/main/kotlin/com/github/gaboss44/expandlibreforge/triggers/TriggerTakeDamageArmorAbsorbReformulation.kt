package com.github.gaboss44.expandlibreforge.triggers

import com.github.gaboss44.expandlibreforge.events.entity.EntityDamageArmorAbsorbReformulationEvent
import com.github.gaboss44.expandlibreforge.extensions.nonEmptyCurrentWeapon
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

sealed class TriggerTakeDamageArmorAbsorbReformulation(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.LOCATION,
        TriggerParameter.EVENT,
        TriggerParameter.VICTIM,
        TriggerParameter.PROJECTILE,
        TriggerParameter.ITEM
    )

    @Suppress("UnstableApiUsage")
    fun handle(event: EntityDamageArmorAbsorbReformulationEvent) {
        val source = event.parent.damageSource
        val victim = source.causingEntity as? LivingEntity ?: source.directEntity as? LivingEntity
        val weapon = victim?.nonEmptyCurrentWeapon
        val projectile = source.directEntity as? Projectile
        this.dispatch(
            event.entity.toDispatcher(),
            TriggerData(
                player = event.entity as? Player,
                victim = victim,
                item = weapon,
                projectile = projectile,
                location = event.entity.location,
                event = event
            )
        )
    }

    companion object {
        fun registerAllInto(category: Triggers) {
            category.register(Default)
            category.register(Monitor)
            category.register(HighestPriority)
            category.register(HighPriority)
            category.register(NormalPriority)
            category.register(LowPriority)
            category.register(LowestPriority)
        }
    }

    object Default : TriggerTakeDamageArmorAbsorbReformulation("take_damage_armor_absorb_reformulation") {
        @EventHandler(ignoreCancelled = true)
        fun onComboEnd(event: EntityDamageArmorAbsorbReformulationEvent) = handle(event)
    }

    object Monitor : TriggerTakeDamageArmorAbsorbReformulation("take_damage_armor_absorb_reformulation_monitor") {
        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        fun onComboEnd(event: EntityDamageArmorAbsorbReformulationEvent) = handle(event)
    }

    object HighestPriority : TriggerTakeDamageArmorAbsorbReformulation("take_damage_armor_absorb_reformulation_highest_priority") {
        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        fun onComboEnd(event: EntityDamageArmorAbsorbReformulationEvent) = handle(event)
    }

    object HighPriority : TriggerTakeDamageArmorAbsorbReformulation("take_damage_armor_absorb_reformulation_high_priority") {
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        fun onComboEnd(event: EntityDamageArmorAbsorbReformulationEvent) = handle(event)
    }

    object NormalPriority : TriggerTakeDamageArmorAbsorbReformulation("take_damage_armor_absorb_reformulation_normal_priority") {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        fun onComboEnd(event: EntityDamageArmorAbsorbReformulationEvent) = handle(event)
    }

    object LowPriority : TriggerTakeDamageArmorAbsorbReformulation("take_damage_armor_absorb_reformulation_low_priority") {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        fun onComboEnd(event: EntityDamageArmorAbsorbReformulationEvent) = handle(event)
    }

    object LowestPriority : TriggerTakeDamageArmorAbsorbReformulation("take_damage_armor_absorb_reformulation_lowest_priority") {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        fun onComboEnd(event: EntityDamageArmorAbsorbReformulationEvent) = handle(event)
    }
}

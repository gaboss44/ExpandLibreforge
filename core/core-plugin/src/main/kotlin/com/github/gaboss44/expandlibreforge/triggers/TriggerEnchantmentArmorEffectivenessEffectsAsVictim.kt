package com.github.gaboss44.expandlibreforge.triggers

import com.github.gaboss44.expandlibreforge.events.entity.EntityEnchantmentArmorEffectivenessEffectsEvent
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

@Suppress("UnstableApiUsage")
sealed class TriggerEnchantmentArmorEffectivenessEffectsAsVictim(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.EVENT,
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM,
        TriggerParameter.LOCATION,
        TriggerParameter.PROJECTILE,
        TriggerParameter.ITEM,
        TriggerParameter.VALUE,
        TriggerParameter.ALT_VALUE
    )

    fun handle(event: EntityEnchantmentArmorEffectivenessEffectsEvent) {
        val source = event.damageSource
        val victim =
            source.causingEntity as? LivingEntity ?:
            source.directEntity as? LivingEntity ?:
            return
        val projectile = source.directEntity as? Projectile
        val location = source.sourceLocation
        this.dispatch(
            victim.toDispatcher(),
            TriggerData(
                event = event,
                player = victim as? Player,
                victim = event.enchantmentUser,
                projectile = projectile,
                item = event.weapon,
                location = location,
                value = event.enchantmentEffectsData.level.value.toDouble(),
                altValue = event.enchantmentEffectsData.originalLevel.toDouble()
            )
        )
    }

    companion object {
        fun registerAllInto(category: Triggers) {
            category.register(Default)
            category.register(LowestPriority)
            category.register(LowPriority)
            category.register(NormalPriority)
            category.register(HighPriority)
            category.register(HighestPriority)
            category.register(Monitor)
        }
    }

    object Default : TriggerEnchantmentArmorEffectivenessEffectsAsVictim("enchantment_armor_effectiveness_effects_as_victim") {
        @EventHandler(ignoreCancelled = true)
        fun onEnchantmentArmorEffectivenessEffects(event: EntityEnchantmentArmorEffectivenessEffectsEvent) = handle(event)
    }

    object LowestPriority : TriggerEnchantmentArmorEffectivenessEffectsAsVictim("enchantment_armor_effectiveness_effects_as_victim_lowest_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
        fun onEnchantmentArmorEffectivenessEffects(event: EntityEnchantmentArmorEffectivenessEffectsEvent) = handle(event)
    }

    object LowPriority : TriggerEnchantmentArmorEffectivenessEffectsAsVictim("enchantment_armor_effectiveness_effects_as_victim_low_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
        fun onEnchantmentArmorEffectivenessEffects(event: EntityEnchantmentArmorEffectivenessEffectsEvent) = handle(event)
    }

    object NormalPriority : TriggerEnchantmentArmorEffectivenessEffectsAsVictim("enchantment_armor_effectiveness_effects_as_victim_normal_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
        fun onEnchantmentArmorEffectivenessEffects(event: EntityEnchantmentArmorEffectivenessEffectsEvent) = handle(event)
    }

    object HighPriority : TriggerEnchantmentArmorEffectivenessEffectsAsVictim("enchantment_armor_effectiveness_effects_as_victim_high_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
        fun onEnchantmentArmorEffectivenessEffects(event: EntityEnchantmentArmorEffectivenessEffectsEvent) = handle(event)
    }

    object HighestPriority : TriggerEnchantmentArmorEffectivenessEffectsAsVictim("enchantment_armor_effectiveness_effects_as_victim_highest_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        fun onEnchantmentArmorEffectivenessEffects(event: EntityEnchantmentArmorEffectivenessEffectsEvent) = handle(event)
    }

    object Monitor : TriggerEnchantmentArmorEffectivenessEffectsAsVictim("enchantment_armor_effectiveness_effects_as_victim_monitor") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        fun onEnchantmentArmorEffectivenessEffects(event: EntityEnchantmentArmorEffectivenessEffectsEvent) = handle(event)
    }
}

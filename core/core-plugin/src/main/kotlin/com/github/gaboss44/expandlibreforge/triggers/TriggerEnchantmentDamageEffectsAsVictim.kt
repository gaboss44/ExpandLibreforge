package com.github.gaboss44.expandlibreforge.triggers

import com.github.gaboss44.expandlibreforge.events.entity.EntityEnchantmentDamageEffectsEvent
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
sealed class TriggerEnchantmentDamageEffectsAsVictim(id: String) : Trigger(id) {
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

    fun handle(event: EntityEnchantmentDamageEffectsEvent) {
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

    object Default : TriggerEnchantmentDamageEffectsAsVictim("enchantment_damage_effects_as_victim") {
        @EventHandler(ignoreCancelled = true)
        fun onEnchantmentDamageEffects(event: EntityEnchantmentDamageEffectsEvent) = handle(event)
    }

    object LowestPriority : TriggerEnchantmentDamageEffectsAsVictim("enchantment_damage_effects_as_victim_lowest_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
        fun onEnchantmentDamageEffects(event: EntityEnchantmentDamageEffectsEvent) = handle(event)
    }

    object LowPriority : TriggerEnchantmentDamageEffectsAsVictim("enchantment_damage_effects_as_victim_low_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
        fun onEnchantmentDamageEffects(event: EntityEnchantmentDamageEffectsEvent) = handle(event)
    }

    object NormalPriority : TriggerEnchantmentDamageEffectsAsVictim("enchantment_damage_effects_as_victim_normal_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
        fun onEnchantmentDamageEffects(event: EntityEnchantmentDamageEffectsEvent) = handle(event)
    }

    object HighPriority : TriggerEnchantmentDamageEffectsAsVictim("enchantment_damage_effects_as_victim_high_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
        fun onEnchantmentDamageEffects(event: EntityEnchantmentDamageEffectsEvent) = handle(event)
    }

    object HighestPriority : TriggerEnchantmentDamageEffectsAsVictim("enchantment_damage_effects_as_victim_highest_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        fun onEnchantmentDamageEffects(event: EntityEnchantmentDamageEffectsEvent) = handle(event)
    }

    object Monitor : TriggerEnchantmentDamageEffectsAsVictim("enchantment_damage_effects_as_victim_monitor") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        fun onEnchantmentDamageEffects(event: EntityEnchantmentDamageEffectsEvent) = handle(event)
    }
}

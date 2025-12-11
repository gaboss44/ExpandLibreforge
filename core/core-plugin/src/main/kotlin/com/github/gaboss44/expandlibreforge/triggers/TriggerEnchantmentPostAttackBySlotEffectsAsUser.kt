package com.github.gaboss44.expandlibreforge.triggers

import com.github.gaboss44.expandlibreforge.events.entity.EntityEnchantmentPostAttackBySlotEffectsEvent
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
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
sealed class TriggerEnchantmentPostAttackBySlotEffectsAsUser(id: String) : Trigger(id) {
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

    fun handle(event: EntityEnchantmentPostAttackBySlotEffectsEvent) {
        val source = event.damageSource
        val victim =
            source.causingEntity as? LivingEntity ?:
            source.directEntity as? LivingEntity
        val projectile = source.directEntity as? Projectile
        val location = source.sourceLocation
        this.dispatch(
            event.enchantmentUser.toDispatcher(),
            TriggerData(
                event = event,
                player = event.enchantmentUser as? Player,
                victim = victim,
                projectile = projectile,
                item = event.enchantmentUser.equipment?.getItem(event.enchantmentEffectsData.slot)?.takeUnless(ItemStack::isEmpty),
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

    object Default : TriggerEnchantmentPostAttackBySlotEffectsAsUser("enchantment_post_attack_by_slot_effects_as_user") {
        @EventHandler(ignoreCancelled = true)
        fun onEnchantmentPostAttackBySlotEffects(event: EntityEnchantmentPostAttackBySlotEffectsEvent) = handle(event)
    }

    object LowestPriority : TriggerEnchantmentPostAttackBySlotEffectsAsUser("enchantment_post_attack_by_slot_effects_as_user_lowest_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
        fun onEnchantmentPostAttackBySlotEffects(event: EntityEnchantmentPostAttackBySlotEffectsEvent) = handle(event)
    }

    object LowPriority : TriggerEnchantmentPostAttackBySlotEffectsAsUser("enchantment_post_attack_by_slot_effects_as_user_low_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
        fun onEnchantmentPostAttackBySlotEffects(event: EntityEnchantmentPostAttackBySlotEffectsEvent) = handle(event)
    }

    object NormalPriority : TriggerEnchantmentPostAttackBySlotEffectsAsUser("enchantment_post_attack_by_slot_effects_as_user_normal_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
        fun onEnchantmentPostAttackBySlotEffects(event: EntityEnchantmentPostAttackBySlotEffectsEvent) = handle(event)
    }

    object HighPriority : TriggerEnchantmentPostAttackBySlotEffectsAsUser("enchantment_post_attack_by_slot_effects_as_user_high_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
        fun onEnchantmentPostAttackBySlotEffects(event: EntityEnchantmentPostAttackBySlotEffectsEvent) = handle(event)
    }

    object HighestPriority : TriggerEnchantmentPostAttackBySlotEffectsAsUser("enchantment_post_attack_by_slot_effects_as_user_highest_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        fun onEnchantmentPostAttackBySlotEffects(event: EntityEnchantmentPostAttackBySlotEffectsEvent) = handle(event)
    }

    object Monitor : TriggerEnchantmentPostAttackBySlotEffectsAsUser("enchantment_post_attack_by_slot_effects_as_user_monitor") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        fun onEnchantmentPostAttackBySlotEffects(event: EntityEnchantmentPostAttackBySlotEffectsEvent) = handle(event)
    }
}

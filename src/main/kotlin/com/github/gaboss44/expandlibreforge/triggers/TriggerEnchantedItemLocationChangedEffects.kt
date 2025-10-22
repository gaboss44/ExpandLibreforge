package com.github.gaboss44.expandlibreforge.triggers

import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import io.papermc.paper.event.entity.EntityEnchantedItemLocationChangedEffectsEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

sealed class TriggerEnchantedItemLocationChangedEffects(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.EVENT,
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM,
        TriggerParameter.VALUE,
        TriggerParameter.ALT_VALUE,
        TriggerParameter.VELOCITY,
        TriggerParameter.LOCATION
    )

    fun handle(event: EntityEnchantedItemLocationChangedEffectsEvent) {
        this.dispatch(
            event.entity.toDispatcher(),
            TriggerData(
                event = event,
                player = event.owner as? Player ?: event.entity as? Player,
                victim = event.entity,
                location = event.entity.location,
                velocity = event.entity.velocity,
                value = event.level.toDouble(),
                altValue = event.overrideLevel.toDouble()
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

    object Default : TriggerEnchantedItemLocationChangedEffects("enchanted_item_location_changed_effects") {
        @EventHandler(ignoreCancelled = true)
        fun onComboStart(event: EntityEnchantedItemLocationChangedEffectsEvent) = handle(event)
    }

    object Monitor : TriggerEnchantedItemLocationChangedEffects("enchanted_item_location_changed_effects_monitor") {
        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        fun onComboStart(event: EntityEnchantedItemLocationChangedEffectsEvent) = handle(event)
    }

    object HighestPriority : TriggerEnchantedItemLocationChangedEffects("enchanted_item_location_changed_effects_highest_priority") {
        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        fun onComboStart(event: EntityEnchantedItemLocationChangedEffectsEvent) = handle(event)
    }

    object HighPriority : TriggerEnchantedItemLocationChangedEffects("enchanted_item_location_changed_effects_high_priority") {
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        fun onComboStart(event: EntityEnchantedItemLocationChangedEffectsEvent) = handle(event)
    }

    object NormalPriority : TriggerEnchantedItemLocationChangedEffects("enchanted_item_location_changed_effects_normal_priority") {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        fun onComboStart(event: EntityEnchantedItemLocationChangedEffectsEvent) = handle(event)
    }

    object LowPriority : TriggerEnchantedItemLocationChangedEffects("enchanted_item_location_changed_effects_low_priority") {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        fun onComboStart(event: EntityEnchantedItemLocationChangedEffectsEvent) = handle(event)
    }

    object LowestPriority : TriggerEnchantedItemLocationChangedEffects("enchanted_item_location_changed_effects_lowest_priority") {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        fun onComboStart(event: EntityEnchantedItemLocationChangedEffectsEvent) = handle(event)
    }
}
package com.github.gaboss44.expandlibreforge.triggers

import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import io.papermc.paper.event.entity.EntityDamageArmorAbsorbEvent
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

sealed class TriggerArmorAbsorb(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.EVENT,
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM,
        TriggerParameter.VALUE,
        TriggerParameter.ALT_VALUE,
        TriggerParameter.VELOCITY,
        TriggerParameter.LOCATION
    )

    fun handle(event: EntityDamageArmorAbsorbEvent) {
        this.dispatch(
            event.entity.toDispatcher(),
            TriggerData(
                event = event,
                player = event.entity as? Player,
                victim = (event.damageSource.causingEntity ?: event.damageSource.directEntity) as? LivingEntity,
                location = event.entity.location,
                velocity = event.entity.velocity,
                value = event.armorPoints.toDouble(),
                altValue = event.armorToughness.toDouble()
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

    object Default : TriggerArmorAbsorb("armor_absorb") {
        @EventHandler(ignoreCancelled = true)
        fun onComboStart(event: EntityDamageArmorAbsorbEvent) = handle(event)
    }

    object Monitor : TriggerArmorAbsorb("armor_absorb_monitor") {
        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        fun onComboStart(event: EntityDamageArmorAbsorbEvent) = handle(event)
    }

    object HighestPriority : TriggerArmorAbsorb("armor_absorb_highest_priority") {
        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        fun onComboStart(event: EntityDamageArmorAbsorbEvent) = handle(event)
    }

    object HighPriority : TriggerArmorAbsorb("armor_absorb_high_priority") {
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        fun onComboStart(event: EntityDamageArmorAbsorbEvent) = handle(event)
    }

    object NormalPriority : TriggerArmorAbsorb("armor_absorb_normal_priority") {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        fun onComboStart(event: EntityDamageArmorAbsorbEvent) = handle(event)
    }

    object LowPriority : TriggerArmorAbsorb("armor_absorb_low_priority") {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        fun onComboStart(event: EntityDamageArmorAbsorbEvent) = handle(event)
    }

    object LowestPriority : TriggerArmorAbsorb("armor_absorb_lowest_priority") {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        fun onComboStart(event: EntityDamageArmorAbsorbEvent) = handle(event)
    }
}
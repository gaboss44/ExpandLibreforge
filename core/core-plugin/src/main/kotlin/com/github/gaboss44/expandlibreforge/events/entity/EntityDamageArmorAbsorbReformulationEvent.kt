@file:Suppress("UnstableApiUsage")

package com.github.gaboss44.expandlibreforge.events.entity

import org.bukkit.damage.DamageSource
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityEvent

class EntityDamageArmorAbsorbReformulationEvent(
    entity: LivingEntity,
    override val parent: EntityDamageEvent,
    armorEffectivenessGetter: (Float, Float, Float) -> Float,
    armorEffectivenessModifier: (LivingEntity, DamageSource, Float) -> Float,
    armorEffectivenessFormula: (Float, Float) -> Float
) : EntityEvent(entity), EntityDamageRelatedEvent, Cancellable {

    var armorEffectivenessGetter = armorEffectivenessGetter
        set(value) {
            field = value
            armorEffectivenessGetterChanged = true
        }

    var armorEffectivenessModifier = armorEffectivenessModifier
        set(value) {
            field = value
            armorEffectivenessModifierChanged = true
        }

    var armorEffectivenessFormula = armorEffectivenessFormula
        set(value) {
            field = value
            armorEffectivenessFormulaChanged = true
        }

    private var armorEffectivenessGetterChanged = false

    fun armorEffectivenessGetterChanged() = this.armorEffectivenessGetterChanged

    private var armorEffectivenessModifierChanged = false

    fun armorEffectivenessModifierChanged() = this.armorEffectivenessModifierChanged

    private var armorEffectivenessFormulaChanged = false

    fun armorEffectivenessFormulaChanged() = this.armorEffectivenessFormulaChanged

    private var cancelled = false

    override fun isCancelled() = cancelled
    override fun setCancelled(cancel: Boolean) { cancelled = cancel }

    override fun getEntity(): LivingEntity = super<EntityEvent>.entity as LivingEntity

    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = handlerList
    }
}

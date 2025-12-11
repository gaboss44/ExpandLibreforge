package com.github.gaboss44.expandlibreforge.events.entity

import org.bukkit.damage.DamageSource
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityEvent

@Suppress("UnstableApiUsage")
class EntityDamageMagicAbsorbReformulationEvent(
    entity: LivingEntity,
    override val parent: EntityDamageEvent,
    damageProtectionModifier: (LivingEntity, DamageSource, Float) -> Float,
    damageProtectionFormula: (Float, Float) -> Float
) : EntityEvent(entity), EntityDamageRelatedEvent, Cancellable {

    private var damageProtectionGetterChanged = false

    fun damageProtectionGetterChanged() = this.damageProtectionGetterChanged

    var damageProtectionModifier = damageProtectionModifier
        set(value) {
            field = value
            damageProtectionGetterChanged = true
        }

    private var damageProtectionFormulaChanged = false

    fun damageProtectionFormulaChanged() = this.damageProtectionFormulaChanged

    var damageProtectionFormula = damageProtectionFormula
        set(value) {
            field = value
            damageProtectionFormulaChanged = true
        }

    private var cancelled = false

    override fun isCancelled(): Boolean {
        return this.cancelled
    }

    override fun setCancelled(cancel: Boolean) {
        this.cancelled = cancel
    }

    override fun getEntity(): LivingEntity {
        return super<EntityEvent>.entity as LivingEntity
    }

    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}

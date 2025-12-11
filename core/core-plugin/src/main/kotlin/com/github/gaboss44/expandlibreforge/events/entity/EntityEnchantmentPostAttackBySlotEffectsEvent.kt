package com.github.gaboss44.expandlibreforge.events.entity

import com.github.gaboss44.expandlibreforge.extensions.EnchantmentEffectsInSlotData
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.HandlerList

@Suppress("UnstableApiUsage")
class EntityEnchantmentPostAttackBySlotEffectsEvent(
    enchantmentUser: LivingEntity,
    override val enchantmentEffectsData: EnchantmentEffectsInSlotData,
    target: Entity,
    damageSource: DamageSource
) : EntityEnchantmentPostAttackEffectEvent(
    enchantmentUser,
    enchantmentEffectsData,
    target,
    damageSource
) {

    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}

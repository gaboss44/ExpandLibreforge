package com.github.gaboss44.expandlibreforge.events.entity

import com.github.gaboss44.expandlibreforge.events.DamageSourceAwareEvent
import com.github.gaboss44.expandlibreforge.extensions.EnchantmentEffectsInSlotData
import org.bukkit.damage.DamageSource
import org.bukkit.entity.LivingEntity
import org.bukkit.event.HandlerList

@Suppress("UnstableApiUsage")
class EntityEnchantmentDamageProtectionEffectsEvent(
    enchantmentUser: LivingEntity,
    enchantmentEffectsData: EnchantmentEffectsInSlotData,
    var protection: Float,
    val originalProtection: Float,
    override val damageSource: DamageSource
) : EntityEnchantmentEffectsEvent(
    enchantmentUser,
    enchantmentEffectsData
), DamageSourceAwareEvent {

    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}

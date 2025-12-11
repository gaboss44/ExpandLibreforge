package com.github.gaboss44.expandlibreforge.events.entity

import com.github.gaboss44.expandlibreforge.events.DamageSourceAwareEvent
import com.github.gaboss44.expandlibreforge.extensions.EnchantmentEffectsData
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.HandlerList

@Suppress("UnstableApiUsage")
abstract class EntityEnchantmentPostAttackEffectEvent(
    enchantmentUser: LivingEntity,
    enchantmentEffectsData: EnchantmentEffectsData,
    open val target: Entity,
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

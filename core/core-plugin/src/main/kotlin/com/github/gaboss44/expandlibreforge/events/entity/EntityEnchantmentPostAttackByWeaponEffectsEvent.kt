package com.github.gaboss44.expandlibreforge.events.entity

import com.github.gaboss44.expandlibreforge.extensions.EnchantmentEffectsData
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
class EntityEnchantmentPostAttackByWeaponEffectsEvent(
    enchantmentUser: LivingEntity,
    enchantmentEffectsData: EnchantmentEffectsData,
    target: Entity,
    weapon: ItemStack,
    damageSource: DamageSource
) : EntityEnchantmentPostAttackEffectEvent(
    enchantmentUser,
    enchantmentEffectsData,
    target,
    damageSource
) {

    var weapon = weapon
        get() = field.clone()

    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}

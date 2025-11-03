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
    val target: Entity,
    weapon: ItemStack,
    val source: DamageSource
) : EntityEnchantmentEffectsEvent(
    enchantmentUser,
    enchantmentEffectsData
) {

    private var _weapon: ItemStack

    var weapon: ItemStack
        get() = _weapon.clone()
        private set(value) { _weapon = value }

    init {
        this._weapon = weapon
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

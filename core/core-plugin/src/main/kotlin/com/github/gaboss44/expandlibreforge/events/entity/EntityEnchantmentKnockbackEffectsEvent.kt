package com.github.gaboss44.expandlibreforge.events.entity

import com.github.gaboss44.expandlibreforge.extensions.EnchantmentEffectsData
import org.apache.commons.lang3.mutable.MutableFloat
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
class EntityEnchantmentKnockbackEffectsEvent(
    enchantmentUser: LivingEntity,
    enchantmentEffectsData: EnchantmentEffectsData,
    val target: Entity,
    val knockback: Float,
    val overrideKnockback: MutableFloat,
    val originalKnockback: Float,
    weapon: ItemStack,
    val source: DamageSource
) : EntityEnchantmentEffectsEvent(
    enchantmentUser,
    enchantmentEffectsData
) {
    constructor(
        enchantmentUser: LivingEntity,
        enchantmentEffectsData: EnchantmentEffectsData,
        target: Entity,
        knockback: Float,
        originalKnockback: Float,
        weapon: ItemStack,
        source: DamageSource
    ) : this(
        enchantmentUser,
        enchantmentEffectsData,
        target,
        knockback,
        MutableFloat(knockback),
        originalKnockback,
        weapon,
        source
    )

    constructor(
        enchantmentUser: LivingEntity,
        enchantmentEffectsData: EnchantmentEffectsData,
        target: Entity,
        overrideKnockback: MutableFloat,
        originalKnockback: Float,
        weapon: ItemStack,
        source: DamageSource
    ) : this(
        enchantmentUser,
        enchantmentEffectsData,
        target,
        overrideKnockback.value,
        overrideKnockback,
        originalKnockback,
        weapon,
        source
    )

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

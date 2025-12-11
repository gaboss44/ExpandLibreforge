package com.github.gaboss44.expandlibreforge.events.entity

import com.github.gaboss44.expandlibreforge.events.DamageSourceAwareEvent
import com.github.gaboss44.expandlibreforge.extensions.EnchantmentEffectsData
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
    var knockback: Float,
    val originalKnockback: Float,
    weapon: ItemStack,
    override val damageSource: DamageSource
) : EntityEnchantmentEffectsEvent(
    enchantmentUser,
    enchantmentEffectsData
), DamageSourceAwareEvent {

    var weapon: ItemStack
        get() = field.clone()
        private set

    init {
        this.weapon = weapon
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

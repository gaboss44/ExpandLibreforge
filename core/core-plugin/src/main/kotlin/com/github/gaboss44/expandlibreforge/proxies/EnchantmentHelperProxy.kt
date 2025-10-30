package com.github.gaboss44.expandlibreforge.proxies

import com.github.gaboss44.expandlibreforge.extensions.EnchantmentEffectsData
import org.apache.commons.lang3.mutable.MutableFloat
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer

@Suppress("UnstableApiUsage")
interface EnchantmentHelperProxy {

    fun modifyDamage(
        damage: Float,
        entity: LivingEntity,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumers : List<Consumer<EnchantmentEffectsData>>
    ) : Float {
        val mutableDamage = MutableFloat(damage)
        modifyDamage(mutableDamage, entity, target, weapon, source, consumers)
        return mutableDamage.toFloat()
    }

    fun modifyDamage(
        damage: MutableFloat,
        entity: LivingEntity,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumers : List<Consumer<EnchantmentEffectsData>>
    )

    fun modifyFallBasedDamage(
        damage: Float,
        entity: LivingEntity,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumers: List<Consumer<EnchantmentEffectsData>>
    ) : Float {
        val mutableDamage = MutableFloat(damage)
        modifyFallBasedDamage(mutableDamage, entity, target, weapon, source, consumers)
        return mutableDamage.toFloat()
    }

    fun modifyFallBasedDamage(
        damage: MutableFloat,
        entity: LivingEntity,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumers: List<Consumer<EnchantmentEffectsData>>
    )
}
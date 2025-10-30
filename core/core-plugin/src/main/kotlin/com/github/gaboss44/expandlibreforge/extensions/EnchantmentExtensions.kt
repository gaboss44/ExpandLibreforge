@file:Suppress("UnstableApiUsage")

package com.github.gaboss44.expandlibreforge.extensions

import com.github.gaboss44.expandlibreforge.proxies.EnchantmentHelperProxy
import org.apache.commons.lang3.mutable.MutableBoolean
import org.apache.commons.lang3.mutable.MutableFloat
import org.apache.commons.lang3.mutable.MutableInt
import org.bukkit.damage.DamageSource
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer

private lateinit var enchantmentHelper: EnchantmentHelperProxy

object EnchantmentExtensions {

    fun setProxyIfNeeded(proxy: EnchantmentHelperProxy) {
        if (!::enchantmentHelper.isInitialized) {
            enchantmentHelper = proxy
        }
    }
}

data class EnchantmentEffectsData(
    val enchantment: Enchantment,
    val level: Int,
    val overrideLevel: MutableInt,
    val forceEffects: MutableBoolean,
    val cancelEffects: MutableBoolean
) {
    constructor(
        enchantment: Enchantment,
        level: Int
    ) : this(
        enchantment,
        level,
        MutableInt(level),
        MutableBoolean(false),
        MutableBoolean(false)
    )
}

fun LivingEntity.modifyDamage(
    damage: Float,
    target: Entity,
    weapon: ItemStack,
    source: DamageSource,
    consumer : Consumer<EnchantmentEffectsData>
) = enchantmentHelper.modifyDamage(
    damage,
    this,
    target,
    weapon,
    source,
    listOf(consumer)
)

fun LivingEntity.modifyDamage(
    damage: Float,
    target: Entity,
    weapon: ItemStack,
    source: DamageSource,
    consumers : List<Consumer<EnchantmentEffectsData>>
) = enchantmentHelper.modifyDamage(
    damage,
    this,
    target,
    weapon,
    source,
    consumers
)

fun LivingEntity.modifyDamage(
    damage: Float,
    target: Entity,
    weapon: ItemStack,
    source: DamageSource,
    vararg consumers : Consumer<EnchantmentEffectsData>
) = modifyDamage(
    damage,
    target,
    weapon,
    source,
    consumers.toList()
)

fun LivingEntity.modifyDamage(
    damage: MutableFloat,
    target: Entity,
    weapon: ItemStack,
    source: DamageSource,
    consumer : Consumer<EnchantmentEffectsData>
) = enchantmentHelper.modifyDamage(
    damage,
    this,
    target,
    weapon,
    source,
    listOf(consumer)
)

fun LivingEntity.modifyDamage(
    damage: MutableFloat,
    target: Entity,
    weapon: ItemStack,
    source: DamageSource,
    consumers : List<Consumer<EnchantmentEffectsData>>
) = enchantmentHelper.modifyDamage(
    damage,
    this,
    target,
    weapon,
    source,
    consumers
)

fun LivingEntity.modifyDamage(
    damage: MutableFloat,
    target: Entity,
    weapon: ItemStack,
    source: DamageSource,
    vararg consumers : Consumer<EnchantmentEffectsData>
) = modifyDamage(
    damage,
    target,
    weapon,
    source,
    consumers.toList()
)

fun LivingEntity.modifyFallBasedDamage(
    damage: Float,
    target: Entity,
    weapon: ItemStack,
    source: DamageSource,
    consumers : List<Consumer<EnchantmentEffectsData>>
) = enchantmentHelper.modifyFallBasedDamage(
    damage,
    this,
    target,
    weapon,
    source,
    consumers
)

fun LivingEntity.modifyFallBasedDamage(
    damage: Float,
    target: Entity,
    weapon: ItemStack,
    source: DamageSource,
    vararg consumers : Consumer<EnchantmentEffectsData>
) = modifyFallBasedDamage(
    damage,
    target,
    weapon,
    source,
    consumers.toList()
)

fun LivingEntity.modifyFallBasedDamage(
    damage: Float,
    target: Entity,
    weapon: ItemStack,
    source: DamageSource,
    consumer : Consumer<EnchantmentEffectsData>
) = enchantmentHelper.modifyFallBasedDamage(
    damage,
    this,
    target,
    weapon,
    source,
    listOf(consumer)
)

fun LivingEntity.modifyFallBasedDamage(
    damage: MutableFloat,
    target: Entity,
    weapon: ItemStack,
    source: DamageSource,
    consumers : List<Consumer<EnchantmentEffectsData>>
) = enchantmentHelper.modifyFallBasedDamage(
    damage,
    this,
    target,
    weapon,
    source,
    consumers
)

fun LivingEntity.modifyFallBasedDamage(
    damage: MutableFloat,
    target: Entity,
    weapon: ItemStack,
    source: DamageSource,
    vararg consumers : Consumer<EnchantmentEffectsData>
) = modifyFallBasedDamage(
    damage,
    target,
    weapon,
    source,
    consumers.toList()
)

fun LivingEntity.modifyFallBasedDamage(
    damage: MutableFloat,
    target: Entity,
    weapon: ItemStack,
    source: DamageSource,
    consumer : Consumer<EnchantmentEffectsData>
) = enchantmentHelper.modifyFallBasedDamage(
    damage,
    this,
    target,
    weapon,
    source,
    listOf(consumer)
)
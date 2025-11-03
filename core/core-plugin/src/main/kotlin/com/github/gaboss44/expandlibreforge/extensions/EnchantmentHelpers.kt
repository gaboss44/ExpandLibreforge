@file:Suppress("UnstableApiUsage")

package com.github.gaboss44.expandlibreforge.extensions

import com.github.gaboss44.expandlibreforge.proxies.EnchantmentHelperProxy
import org.apache.commons.lang3.mutable.MutableBoolean
import org.apache.commons.lang3.mutable.MutableFloat
import org.apache.commons.lang3.mutable.MutableInt
import org.bukkit.World
import org.bukkit.damage.DamageSource
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer

private lateinit var enchantmentHelper: EnchantmentHelperProxy

object EnchantmentHelpers {

    fun setProxyIfNeeded(proxy: EnchantmentHelperProxy) {
        if (!::enchantmentHelper.isInitialized) {
            enchantmentHelper = proxy
        }
    }

    fun modifyDamage(
        damage: MutableFloat,
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumer : Consumer<EnchantmentEffectsData>
    ) = this.modifyDamage(
        damage,
        world,
        target,
        weapon,
        source,
        listOf(consumer)
    )

    fun modifyDamage(
        damage: MutableFloat,
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        vararg consumers : Consumer<EnchantmentEffectsData>
    ) = this.modifyDamage(
        damage,
        world,
        target,
        weapon,
        source,
        consumers.toList()
    )

    fun modifyDamage(
        damage: MutableFloat,
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumers : List<Consumer<EnchantmentEffectsData>>
    ) = enchantmentHelper.modifyDamage(
        damage,
        world,
        target,
        weapon,
        source,
        consumers
    )

    fun modifyFallBasedDamage(
        damage: MutableFloat,
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumers : List<Consumer<EnchantmentEffectsData>>
    ) = enchantmentHelper.modifyFallBasedDamage(
        damage,
        world,
        target,
        weapon,
        source,
        consumers
    )

    fun modifyFallBasedDamage(
        damage: MutableFloat,
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        vararg consumers : Consumer<EnchantmentEffectsData>
    ) = this.modifyFallBasedDamage(
        damage,
        world,
        target,
        weapon,
        source,
        consumers.toList()
    )

    fun modifyFallBasedDamage(
        damage: MutableFloat,
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumer : Consumer<EnchantmentEffectsData>
    ) = this.modifyFallBasedDamage(
        damage,
        world,
        target,
        weapon,
        source,
        listOf(consumer)
    )

    fun modifyKnockback(
        knockback: MutableFloat,
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumers : List<Consumer<EnchantmentEffectsData>>
    ) = enchantmentHelper.modifyKnockback(
        knockback,
        world,
        target,
        weapon,
        source,
        consumers
    )

    fun modifyKnockback(
        knockback: MutableFloat,
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        vararg consumers : Consumer<EnchantmentEffectsData>
    ) = this.modifyKnockback(
        knockback,
        world,
        target,
        weapon,
        source,
        consumers.toList()
    )

    fun modifyKnockback(
        knockback: MutableFloat,
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumer : Consumer<EnchantmentEffectsData>
    ) = this.modifyKnockback(
        knockback,
        world,
        target,
        weapon,
        source,
        listOf(consumer)
    )

    fun mutateInvulnerabilityToDamage(
        invulnerable: MutableBoolean,
        world: World,
        entity: LivingEntity,
        source: DamageSource,
        consumers: List<Consumer<EnchantmentEffectsInSlotData>>
    ) = enchantmentHelper.mutateInvulnerabilityToDamage(
        invulnerable,
        world,
        entity,
        source,
        consumers
    )

    fun mutateInvulnerabilityToDamage(
        invulnerable: MutableBoolean,
        world: World,
        entity: LivingEntity,
        source: DamageSource,
        vararg consumers: Consumer<EnchantmentEffectsInSlotData>
    ) = this.mutateInvulnerabilityToDamage(
        invulnerable,
        world,
        entity,
        source,
        consumers.toList()
    )

    fun mutateInvulnerabilityToDamage(
        invulnerable: MutableBoolean,
        world: World,
        entity: LivingEntity,
        source: DamageSource,
        consumer: Consumer<EnchantmentEffectsInSlotData>
    ) = this.mutateInvulnerabilityToDamage(
        invulnerable,
        world,
        entity,
        source,
        listOf(consumer)
    )

    fun doPostAttackEffects(
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        byWeaponConsumers: List<Consumer<EnchantmentEffectsData>> = emptyList(),
        bySlotConsumers: List<Consumer<EnchantmentEffectsInSlotData>> = emptyList()
    ) = enchantmentHelper.doPostAttackEffects(
        world,
        target,
        weapon,
        source,
        byWeaponConsumers,
        bySlotConsumers
    )
}

open class EnchantmentEffectsData(
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

class EnchantmentEffectsInSlotData(
    enchantment: Enchantment,
    level: Int,
    val slot: EquipmentSlot,
    overrideLevel: MutableInt,
    forceEffects: MutableBoolean,
    val forceSlot: MutableBoolean,
    cancelEffects: MutableBoolean
) : EnchantmentEffectsData(
    enchantment,
    level,
    overrideLevel,
    forceEffects,
    cancelEffects
) {
    constructor(
        enchantment: Enchantment,
        level: Int,
        slot: EquipmentSlot
    ) : this(
        enchantment,
        level,
        slot,
        MutableInt(level),
        MutableBoolean(false),
        MutableBoolean(false),
        MutableBoolean(false)
    )
}
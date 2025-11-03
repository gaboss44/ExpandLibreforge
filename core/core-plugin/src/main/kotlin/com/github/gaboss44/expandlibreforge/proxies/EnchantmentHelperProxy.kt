package com.github.gaboss44.expandlibreforge.proxies

import com.github.gaboss44.expandlibreforge.extensions.EnchantmentEffectsData
import com.github.gaboss44.expandlibreforge.extensions.EnchantmentEffectsInSlotData
import org.apache.commons.lang3.mutable.MutableBoolean
import org.apache.commons.lang3.mutable.MutableFloat
import org.bukkit.World
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer

@Suppress("UnstableApiUsage")
interface EnchantmentHelperProxy {

    fun modifyDamage(
        damage: MutableFloat,
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumers : List<Consumer<EnchantmentEffectsData>>
    )

    fun modifyFallBasedDamage(
        damage: MutableFloat,
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumers: List<Consumer<EnchantmentEffectsData>>
    )

    fun modifyKnockback(
        knockback: MutableFloat,
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumers: List<Consumer<EnchantmentEffectsData>>
    )

    fun mutateInvulnerabilityToDamage(
        invulnerable: MutableBoolean,
        world: World,
        entity: LivingEntity,
        source: DamageSource,
        consumers: List<Consumer<EnchantmentEffectsInSlotData>>
    )

    fun doPostAttackEffects(
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        byWeaponConsumers: List<Consumer<EnchantmentEffectsData>>,
        bySlotConsumers: List<Consumer<EnchantmentEffectsInSlotData>>
    )
}
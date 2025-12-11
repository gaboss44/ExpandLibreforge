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

    @JvmStatic
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

    @JvmStatic
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

    @JvmStatic
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

    @JvmStatic
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

    @JvmStatic
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

    @JvmStatic
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

    @JvmStatic
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

    @JvmStatic
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

    @JvmStatic
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

    @JvmStatic
    fun modifyDamageProtection(
        protection: MutableFloat,
        world: World,
        target: LivingEntity,
        source: DamageSource,
        consumers: List<Consumer<EnchantmentEffectsInSlotData>>
    ) = enchantmentHelper.modifyDamageProtection(
        protection,
        world,
        target,
        source,
        consumers
    )

    @JvmStatic
    fun modifyDamageProtection(
        protection: MutableFloat,
        world: World,
        target: LivingEntity,
        source: DamageSource,
        vararg consumers: Consumer<EnchantmentEffectsInSlotData>
    ) = this.modifyDamageProtection(
        protection,
        world,
        target,
        source,
        consumers.toList()
    )

    @JvmStatic
    fun modifyDamageProtection(
        protection: MutableFloat,
        world: World,
        target: LivingEntity,
        source: DamageSource,
        consumer: Consumer<EnchantmentEffectsInSlotData>
    ) = this.modifyDamageProtection(
        protection,
        world,
        target,
        source,
        listOf(consumer)
    )

    @JvmStatic
    fun modifyArmorEffectiveness(
        effectiveness: MutableFloat,
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumers: List<Consumer<EnchantmentEffectsData>>
    ) = enchantmentHelper.modifyArmorEffectiveness(
        effectiveness,
        world,
        target,
        weapon,
        source,
        consumers
    )

    @JvmStatic
    fun modifyArmorEffectiveness(
        effectiveness: MutableFloat,
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        vararg consumers: Consumer<EnchantmentEffectsData>
    ) = enchantmentHelper.modifyArmorEffectiveness(
        effectiveness,
        world,
        target,
        weapon,
        source,
        consumers.toList()
    )

    @JvmStatic
    fun modifyArmorEffectiveness(
        effectiveness: MutableFloat,
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumer: Consumer<EnchantmentEffectsData>
    ) = enchantmentHelper.modifyArmorEffectiveness(
        effectiveness,
        world,
        target,
        weapon,
        source,
        listOf(consumer)
    )

    @JvmStatic
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

    @JvmStatic
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

    @JvmStatic
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

    @JvmStatic
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

interface EnchantmentEffectsData {
    val enchantment: Enchantment
    val originalLevel: Int
    val level: MutableInt
    val forceEffects: MutableBoolean
    val cancelEffects: MutableBoolean

    companion object {
        operator fun invoke(
            enchantment: Enchantment,
            originalLevel: Int,
            level: MutableInt,
            forceEffects: MutableBoolean,
            cancelEffects: MutableBoolean
        ) : EnchantmentEffectsData = EnchantmentEffectsDataRecord(
            enchantment,
            originalLevel,
            level,
            forceEffects,
            cancelEffects
        )

        operator fun invoke(
            enchantment: Enchantment,
            level: Int
        ) : EnchantmentEffectsData = EnchantmentEffectsDataRecord(
            enchantment,
            level
        )
    }
}

interface EnchantmentEffectsInSlotData : EnchantmentEffectsData {
    val slot: EquipmentSlot
    val forceSlot: MutableBoolean

    companion object {
        operator fun invoke(
            enchantment: Enchantment,
            originalLevel: Int,
            level: MutableInt,
            slot: EquipmentSlot,
            forceEffects: MutableBoolean,
            cancelEffects: MutableBoolean,
            forceSlot: MutableBoolean
        ) : EnchantmentEffectsInSlotData = EnchantmentEffectsInSlotDataRecord(
            enchantment,
            originalLevel,
            level,
            slot,
            forceEffects,
            cancelEffects,
            forceSlot
        )

        operator fun invoke(
            enchantment: Enchantment,
            level: Int,
            slot: EquipmentSlot
        ) : EnchantmentEffectsInSlotData = EnchantmentEffectsInSlotDataRecord(
            enchantment,
            level,
            slot,
        )
    }
}

@JvmRecord
data class EnchantmentEffectsDataRecord(
    override val enchantment: Enchantment,
    override val originalLevel: Int,
    override val level: MutableInt,
    override val forceEffects: MutableBoolean,
    override val cancelEffects: MutableBoolean
) : EnchantmentEffectsData {
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

@JvmRecord
data class EnchantmentEffectsInSlotDataRecord(
    override val enchantment: Enchantment,
    override val originalLevel: Int,
    override val level: MutableInt,
    override val slot: EquipmentSlot,
    override val forceEffects: MutableBoolean,
    override val cancelEffects: MutableBoolean,
    override val forceSlot: MutableBoolean
) : EnchantmentEffectsInSlotData {
    constructor(
        enchantment: Enchantment,
        level: Int,
        slot: EquipmentSlot
    ) : this(
        enchantment,
        level,
        MutableInt(level),
        slot,
        MutableBoolean(false),
        MutableBoolean(false),
        MutableBoolean(false)
    )
}
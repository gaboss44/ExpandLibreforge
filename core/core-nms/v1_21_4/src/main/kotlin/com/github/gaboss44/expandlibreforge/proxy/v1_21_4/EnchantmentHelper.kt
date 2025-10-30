package com.github.gaboss44.expandlibreforge.proxy.v1_21_4

import com.github.gaboss44.expandlibreforge.extensions.EnchantmentEffectsData
import com.github.gaboss44.expandlibreforge.proxies.EnchantmentHelperProxy
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.enchantment.ConditionalEffect
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantment.damageContext
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect
import net.minecraft.world.level.storage.loot.LootContext
import org.apache.commons.lang3.mutable.MutableFloat
import org.bukkit.craftbukkit.damage.CraftDamageSource
import org.bukkit.craftbukkit.enchantments.CraftEnchantment
import org.bukkit.craftbukkit.entity.CraftEntity
import org.bukkit.craftbukkit.entity.CraftLivingEntity
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer

@Suppress("UnstableApiUsage")
class EnchantmentHelper : EnchantmentHelperProxy {

    override fun modifyDamage(
        damage: MutableFloat,
        entity: LivingEntity,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumers: List<Consumer<EnchantmentEffectsData>>
    ) {
        val nmsEntity = (entity as CraftLivingEntity).handle!!
        val nmsLevel = nmsEntity.level() as? ServerLevel ?: return
        val nmsTarget = (target as CraftEntity).handle!!
        val nmsWeapon = (weapon as CraftItemStack).handle!!
        val nmsSource = (source as CraftDamageSource).handle!!

        nmsWeapon.runIterationOnEnchantmentEffects(
            component = EnchantmentEffectComponents.DAMAGE,
            consumers = consumers
        ) { data, effect ->
            if (data.cancelEffects.isTrue) return@runIterationOnEnchantmentEffects
            if (data.forceEffects.isTrue ||
                effect.matches(damageContext(
                    nmsLevel, data.overrideLevel.value, nmsTarget, nmsSource
                ))) {
                damage.value = effect.effect().process(data.overrideLevel.value, nmsTarget.getRandom(), damage.value)
            }
        }
    }

    override fun modifyFallBasedDamage(
        damage: MutableFloat,
        entity: LivingEntity,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumers: List<Consumer<EnchantmentEffectsData>>
    ) {
        val nmsEntity = (entity as CraftLivingEntity).handle!!
        val nmsLevel = nmsEntity.level() as? ServerLevel ?: return
        val nmsTarget = (target as CraftEntity).handle!!
        val nmsWeapon = (weapon as CraftItemStack).handle!!
        val nmsSource = (source as CraftDamageSource).handle!!

        nmsWeapon.runIterationOnEnchantmentEffects(
            component = EnchantmentEffectComponents.SMASH_DAMAGE_PER_FALLEN_BLOCK,
            consumers = consumers
        ) { data, effect ->
            if (data.cancelEffects.isTrue) return@runIterationOnEnchantmentEffects
            if (data.forceEffects.isTrue ||
                effect.matches(damageContext(
                    nmsLevel, data.overrideLevel.value, nmsTarget, nmsSource
                ))) {
                damage.value = effect.effect().process(data.overrideLevel.value, nmsTarget.getRandom(), damage.value)
            }
        }
    }

    fun <T> net.minecraft.world.item.ItemStack.runIterationOnEnchantmentEffects(
        component: DataComponentType<List<T>>,
        consumers: List<Consumer<EnchantmentEffectsData>>,
        visitor: (EnchantmentEffectsData, T) -> Unit
    ) {
        val enchantments = this.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
        for (entry in enchantments.entrySet()) {
            val enchantmentEffects = entry.key!!.value().getEffects(component)
            if (enchantmentEffects.isEmpty()) continue

            val enchantmentEffectsData = EnchantmentEffectsData(
                enchantment = CraftEnchantment.minecraftHolderToBukkit(entry.key!!),
                level = entry.intValue
            )

            consumers.forEach { it.accept(enchantmentEffectsData) }

            for (effect in enchantmentEffects) {
                visitor.invoke(enchantmentEffectsData, effect)
            }
        }
    }


    /*
        Unused
    */

    fun net.minecraft.world.item.ItemStack.runIterationOnEnchantments(
        consumers: List<Consumer<EnchantmentEffectsData>>,
        visitor: (Holder<Enchantment>, EnchantmentEffectsData) -> Unit
    ) {
        val enchantments = this.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
        for (entry in enchantments.entrySet()) {
            val enchantmentEffectsData = EnchantmentEffectsData(
                enchantment = CraftEnchantment.minecraftHolderToBukkit(entry.key!!),
                level = entry.intValue
            )

            consumers.forEach { it.accept(enchantmentEffectsData) }

            visitor.invoke(entry.key!!, enchantmentEffectsData)
        }
    }

    fun Enchantment.modifyDamage(
        level: ServerLevel,
        enchantmentLevel: Int,
        entity: net.minecraft.world.entity.Entity,
        damageSource: net.minecraft.world.damagesource.DamageSource,
        value: MutableFloat,
        forceEffects: Boolean = false
    ) {
        if (forceEffects) {
            modifyValue(
                EnchantmentEffectComponents.DAMAGE,
                enchantmentLevel,
                entity,
                value
            )
        } else {
            modifyDamageFilteredValue(
                EnchantmentEffectComponents.DAMAGE,
                level,
                enchantmentLevel,
                entity,
                damageSource,
                value
            )
        }
    }

    fun Enchantment.modifyFallBasedDamage(
        level: ServerLevel,
        enchantmentLevel: Int,
        entity: net.minecraft.world.entity.Entity,
        damageSource: net.minecraft.world.damagesource.DamageSource,
        value: MutableFloat,
        forceEffects: Boolean = false
    ) {
        if (forceEffects) {
            modifyValue(
                EnchantmentEffectComponents.SMASH_DAMAGE_PER_FALLEN_BLOCK,
                enchantmentLevel,
                entity,
                value
            )
        } else {
            modifyDamageFilteredValue(
                EnchantmentEffectComponents.SMASH_DAMAGE_PER_FALLEN_BLOCK,
                level,
                enchantmentLevel,
                entity,
                damageSource,
                value
            )
        }
    }

    fun Enchantment.modifyValue(
        component: DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>>,
        enchantmentLevel: Int,
        entity: net.minecraft.world.entity.Entity,
        value: MutableFloat
    ) {
        applyEffects(
            this.getEffects(component)
        ) { effect ->
            value.value = effect.process(
                enchantmentLevel,
                entity.getRandom(),
                value.toFloat()
            )
        }
    }

    fun Enchantment.modifyDamageFilteredValue(
        component: DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>>,
        level: ServerLevel,
        enchantmentLevel: Int,
        entity: net.minecraft.world.entity.Entity,
        damageSource: net.minecraft.world.damagesource.DamageSource,
        value: MutableFloat
    ) {
        applyEffects(
            this.getEffects(component),
            damageContext(level, enchantmentLevel, entity, damageSource)
        ) { effect ->
            value.value = effect.process(
                enchantmentLevel,
                entity.getRandom(),
                value.toFloat()
            )
        }
    }

    fun <T> applyEffects(effects: List<ConditionalEffect<T>>, context: LootContext? = null, applier: Consumer<T>) {
        for (effect in effects) {
            if (context == null || effect.matches(context)) {
                applier.accept(effect.effect())
            }
        }
    }
}

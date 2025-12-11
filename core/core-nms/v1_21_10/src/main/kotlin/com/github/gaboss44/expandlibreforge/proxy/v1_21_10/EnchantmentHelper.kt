package com.github.gaboss44.expandlibreforge.proxy.v1_21_10

import com.github.gaboss44.expandlibreforge.extensions.EnchantmentEffectsData
import com.github.gaboss44.expandlibreforge.extensions.EnchantmentEffectsInSlotData
import com.github.gaboss44.expandlibreforge.proxies.EnchantmentHelperProxy
import com.github.gaboss44.expandlibreforge.proxy.common.toNMS
import com.github.gaboss44.expandlibreforge.proxy.common.unwrapNMS
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.ConditionalEffect
import net.minecraft.world.item.enchantment.EnchantedItemInUse
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantment.damageContext
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents
import net.minecraft.world.item.enchantment.EnchantmentTarget
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.item.enchantment.TargetedConditionalEffect
import net.minecraft.world.level.storage.loot.LootContext
import org.apache.commons.lang3.mutable.MutableBoolean
import org.apache.commons.lang3.mutable.MutableFloat
import org.bukkit.World
import org.bukkit.craftbukkit.CraftEquipmentSlot
import org.bukkit.craftbukkit.enchantments.CraftEnchantment
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer

@Suppress("UnstableApiUsage")
class EnchantmentHelper : EnchantmentHelperProxy {

    override fun modifyDamage(
        damage: MutableFloat,
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumers: List<Consumer<EnchantmentEffectsData>>
    ) {

        weapon.unwrapNMS().runIterationOnEnchantmentConditionalEffects(
            component = EnchantmentEffectComponents.DAMAGE,
            context = { damageContext(world.toNMS(), it.level.value, target.toNMS(), source.toNMS()) },
            consumers = consumers
        ) { data, _, effect ->
            damage.value = effect.effect.process(
                data.level.value,
                target.toNMS().getRandom(),
                damage.value
            )
        }
    }

    override fun modifyFallBasedDamage(
        damage: MutableFloat,
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumers: List<Consumer<EnchantmentEffectsData>>
    ) {
        weapon.unwrapNMS().runIterationOnEnchantmentConditionalEffects(
            component = EnchantmentEffectComponents.SMASH_DAMAGE_PER_FALLEN_BLOCK,
            context = { damageContext(world.toNMS(), it.level.value, target.toNMS(), source.toNMS()) },
            consumers = consumers
        ) { data, _, effect ->
            damage.value = effect.effect.process(
                data.level.value,
                target.toNMS().getRandom(),
                damage.value
            )
        }
    }

    override fun modifyKnockback(
        knockback: MutableFloat,
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumers: List<Consumer<EnchantmentEffectsData>>
    ) {
        weapon.unwrapNMS().runIterationOnEnchantmentConditionalEffects(
            component = EnchantmentEffectComponents.KNOCKBACK,
            context = { damageContext(world.toNMS(), it.level.value, target.toNMS(), source.toNMS()) },
            consumers = consumers
        ) { data, _, effect ->
            knockback.value = effect.effect.process(
                data.level.value,
                target.toNMS().getRandom(),
                knockback.value
            )
        }
    }

    override fun modifyDamageProtection(
        protection: MutableFloat,
        world: World,
        target: LivingEntity,
        source: DamageSource,
        consumers: List<Consumer<EnchantmentEffectsInSlotData>>
    ) {
        target.toNMS().runIterationOnEnchantmentConditionalEffects(
            component = EnchantmentEffectComponents.DAMAGE_PROTECTION,
            context = { damageContext(world.toNMS(), it.level.value, target.toNMS(), source.toNMS()) },
            consumers = consumers
        ) { data, _, _, effect ->
            protection.value = effect.effect.process(
                data.level.value,
                target.toNMS().getRandom(),
                protection.value
            )
        }
    }

    override fun modifyArmorEffectiveness(
        effectiveness: MutableFloat,
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        consumers: List<Consumer<EnchantmentEffectsData>>
    ) {
        weapon.unwrapNMS().runIterationOnEnchantmentConditionalEffects(
            component = EnchantmentEffectComponents.ARMOR_EFFECTIVENESS,
            context = { damageContext(world.toNMS(), it.level.value, target.toNMS(), source.toNMS()) },
            consumers = consumers
        ) { data, _, effect ->
            effectiveness.value = effect.effect.process(
                data.level.value,
                target.toNMS().getRandom(),
                effectiveness.value
            )
        }
    }

    override fun mutateInvulnerabilityToDamage(
        invulnerable: MutableBoolean,
        world: World,
        entity: LivingEntity,
        source: DamageSource,
        consumers: List<Consumer<EnchantmentEffectsInSlotData>>
    ) {
        entity.toNMS().runIterationOnEnchantmentConditionalEffects(
            component = EnchantmentEffectComponents.DAMAGE_IMMUNITY,
            context = { damageContext(world.toNMS(), it.level.value, entity.toNMS(), source.toNMS()) },
            consumers = consumers
        ) { data, _, _, _ ->
            invulnerable.value = true
        }
    }

    override fun doPostAttackEffects(
        world: World,
        target: Entity,
        weapon: ItemStack,
        source: DamageSource,
        byWeaponConsumers: List<Consumer<EnchantmentEffectsData>>,
        bySlotConsumers: List<Consumer<EnchantmentEffectsInSlotData>>
    ) {
        doPostAttackEffectsWithWeapon(
            world.toNMS(),
            target.toNMS(),
            weapon.unwrapNMS(),
            source.toNMS(),
            byWeaponConsumers,
            bySlotConsumers
        )
    }


    fun doPostAttackEffects(
        level: ServerLevel,
        entity: net.minecraft.world.entity.Entity,
        source: net.minecraft.world.damagesource.DamageSource,
        byWeaponConsumers: List<Consumer<EnchantmentEffectsData>>,
        bySlotConsumers: List<Consumer<EnchantmentEffectsInSlotData>>
    ) {
        val attacker = source.entity
        if (attacker is net.minecraft.world.entity.LivingEntity) {
            doPostAttackEffectsWithWeapon(level, entity, attacker.weaponItem, source, byWeaponConsumers, bySlotConsumers)
        } else {
            doPostAttackEffects(level, entity, source, bySlotConsumers)
        }
    }

    fun doPostAttackEffectsWithWeapon(
        level: ServerLevel,
        entity: net.minecraft.world.entity.Entity,
        weapon: net.minecraft.world.item.ItemStack,
        source: net.minecraft.world.damagesource.DamageSource,
        byWeaponConsumers: List<Consumer<EnchantmentEffectsData>>,
        bySlotConsumers: List<Consumer<EnchantmentEffectsInSlotData>>
    ) {
        doPostAttackEffectsWithWeaponOnBreak(level, entity, weapon,source, null, byWeaponConsumers, bySlotConsumers)
    }

    fun doPostAttackEffects(
        level: ServerLevel,
        entity: net.minecraft.world.entity.Entity,
        source: net.minecraft.world.damagesource.DamageSource,
        bySlotConsumers: List<Consumer<EnchantmentEffectsInSlotData>>
    ) {
        if (entity is net.minecraft.world.entity.LivingEntity) {
            entity.runIterationOnEnchantmentTargetedConditionalEffects(
                component = EnchantmentEffectComponents.POST_ATTACK,
                context = { damageContext(level, it.level.value, entity, source) },
                consumers = bySlotConsumers
            ) { data, holder, slot, effect ->
                effect.effect.apply(
                    level,
                    data.level.value,
                    EnchantedItemInUse(entity.getItemBySlot(slot), slot, entity),
                    entity,
                    entity.position()
                )
            }
        }
    }

    fun doPostAttackEffectsWithWeaponOnBreak(
        level: ServerLevel,
        entity: net.minecraft.world.entity.Entity,
        weapon: net.minecraft.world.item.ItemStack,
        source: net.minecraft.world.damagesource.DamageSource,
        onBreak: ((net.minecraft.world.item.Item) -> Unit)?,
        byWeaponConsumers: List<Consumer<EnchantmentEffectsData>>,
        bySlotConsumers: List<Consumer<EnchantmentEffectsInSlotData>>
    ) {
        doPostAttackEffects(level, entity, source, bySlotConsumers)

        val attacker = source.entity
        if (attacker is net.minecraft.world.entity.LivingEntity) {
            weapon.runIterationOnEnchantmentTargetedConditionalEffects(
                component = EnchantmentEffectComponents.POST_ATTACK,
                context = { damageContext(level, it.level.value, entity, source) },
                consumers = byWeaponConsumers
            ) { data, holder, effect ->
                val affected = when (effect.affected) {
                    EnchantmentTarget.ATTACKER -> source.entity
                    EnchantmentTarget.DAMAGING_ENTITY -> source.directEntity
                    EnchantmentTarget.VICTIM -> entity
                } ?: return@runIterationOnEnchantmentTargetedConditionalEffects
                effect.effect.apply(level, data.level.value, EnchantedItemInUse(weapon, EquipmentSlot.MAINHAND, attacker), affected, affected.position())
            }
        } else if (onBreak != null) {
            weapon.runIterationOnEnchantmentTargetedConditionalEffects(
                component = EnchantmentEffectComponents.POST_ATTACK,
                context = { damageContext(level, it.level.value, entity, source) },
                consumers = byWeaponConsumers
            ) { data, holder, effect ->
                val affected = when (effect.affected) {
                    EnchantmentTarget.ATTACKER -> source.entity
                    EnchantmentTarget.DAMAGING_ENTITY -> source.directEntity
                    EnchantmentTarget.VICTIM -> entity
                } ?: return@runIterationOnEnchantmentTargetedConditionalEffects
                effect.effect.apply(level, data.level.value, EnchantedItemInUse(weapon, null, null, onBreak), affected, affected.position())
            }
        }
    }

    fun <T : ConditionalEffect<*>> net.minecraft.world.item.ItemStack.runIterationOnEnchantmentConditionalEffects(
        component: DataComponentType<List<T>>,
        context: (EnchantmentEffectsData) -> LootContext,
        consumers: List<Consumer<EnchantmentEffectsData>>,
        onConsume: (EnchantmentEffectsData) -> Unit = {},
        visitor: (EnchantmentEffectsData, Holder<Enchantment>, T) -> Unit
    ) {
        val enchantments = this.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
        for (entry in enchantments.entrySet()) {
            val enchantmentEffects = entry.key!!.value().getEffects(component)
            if (enchantmentEffects.isEmpty()) {
                continue
            }

            val enchantmentEffectsData = EnchantmentEffectsData(
                enchantment = CraftEnchantment.minecraftHolderToBukkit(entry.key!!),
                level = entry.intValue
            )

            consumers.forEach { it.accept(enchantmentEffectsData) }
            onConsume(enchantmentEffectsData)
            if (enchantmentEffectsData.cancelEffects.isTrue) {
                continue
            }

            val context = context(enchantmentEffectsData)
            for (effect in enchantmentEffects) {
                if (enchantmentEffectsData.cancelEffects.isTrue) {
                    continue
                }
                if (enchantmentEffectsData.forceEffects.isFalse && !effect.matches(context)) {
                    continue
                }
                visitor.invoke(enchantmentEffectsData, entry.key, effect)
            }
        }
    }

    fun <T : TargetedConditionalEffect<*>> net.minecraft.world.item.ItemStack.runIterationOnEnchantmentTargetedConditionalEffects(
        component: DataComponentType<List<T>>,
        context: (EnchantmentEffectsData) -> LootContext,
        consumers: List<Consumer<EnchantmentEffectsData>>,
        onConsume: (EnchantmentEffectsData) -> Unit = {},
        visitor: (EnchantmentEffectsData, Holder<Enchantment>, T) -> Unit
    ) {
        val enchantments = this.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
        for (entry in enchantments.entrySet()) {
            val enchantmentEffects = entry.key!!.value().getEffects(component)
            if (enchantmentEffects.isEmpty()) {
                continue
            }

            val enchantmentEffectsData = EnchantmentEffectsData(
                enchantment = CraftEnchantment.minecraftHolderToBukkit(entry.key!!),
                level = entry.intValue
            )

            consumers.forEach { it.accept(enchantmentEffectsData) }
            onConsume(enchantmentEffectsData)
            if (enchantmentEffectsData.cancelEffects.isTrue) {
                continue
            }

            val context = context(enchantmentEffectsData)
            for (effect in enchantmentEffects) {
                if (enchantmentEffectsData.cancelEffects.isTrue) {
                    continue
                }
                if (enchantmentEffectsData.forceEffects.isFalse && !effect.matches(context)) {
                    continue
                }
                visitor.invoke(enchantmentEffectsData, entry.key, effect)
            }
        }
    }

    fun <T : ConditionalEffect<*>> net.minecraft.world.item.ItemStack.runIterationOnEnchantmentConditionalEffects(
        component: DataComponentType<List<T>>,
        slot: EquipmentSlot,
        context: (EnchantmentEffectsInSlotData) -> LootContext,
        consumers: List<Consumer<EnchantmentEffectsInSlotData>>,
        onConsume: (EnchantmentEffectsInSlotData) -> Unit = {},
        visitor: (EnchantmentEffectsInSlotData, Holder<Enchantment>, T) -> Unit
    ) {
        val enchantments = this.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
        for (entry in enchantments.entrySet()) {
            val enchantmentEffects = entry.key!!.value().getEffects(component)
            if (enchantmentEffects.isEmpty()) {
                continue
            }

            val enchantmentEffectsData = EnchantmentEffectsInSlotData(
                enchantment = CraftEnchantment.minecraftHolderToBukkit(entry.key!!),
                level = entry.intValue,
                slot = CraftEquipmentSlot.getSlot(slot)
            )

            consumers.forEach { it.accept(enchantmentEffectsData) }
            onConsume(enchantmentEffectsData)
            if (enchantmentEffectsData.cancelEffects.isTrue) {
                continue
            }
            val matchesSlot = entry.key.value().matchingSlot(slot)
            if (enchantmentEffectsData.forceSlot.isFalse && !matchesSlot) {
                continue
            }

            val context = context(enchantmentEffectsData)
            for (effect in enchantmentEffects) {
                if (enchantmentEffectsData.cancelEffects.isTrue) {
                    continue
                }
                if (enchantmentEffectsData.forceSlot.isFalse && !matchesSlot) {
                    continue
                }
                if (enchantmentEffectsData.forceEffects.isFalse && !effect.matches(context)) {
                    continue
                }
                visitor.invoke(enchantmentEffectsData, entry.key, effect)
            }
        }
    }

    fun <T : TargetedConditionalEffect<*>> net.minecraft.world.item.ItemStack.runIterationOnEnchantmentTargetedConditionalEffects(
        component: DataComponentType<List<T>>,
        slot: EquipmentSlot,
        context: (EnchantmentEffectsInSlotData) -> LootContext,
        consumers: List<Consumer<EnchantmentEffectsInSlotData>>,
        onConsume: (EnchantmentEffectsInSlotData) -> Unit = {},
        visitor: (EnchantmentEffectsInSlotData, Holder<Enchantment>, T) -> Unit
    ) {
        val enchantments = this.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
        for (entry in enchantments.entrySet()) {
            val enchantmentEffects = entry.key!!.value().getEffects(component)
            if (enchantmentEffects.isEmpty()) {
                continue
            }

            val enchantmentEffectsData = EnchantmentEffectsInSlotData(
                enchantment = CraftEnchantment.minecraftHolderToBukkit(entry.key!!),
                level = entry.intValue,
                slot = CraftEquipmentSlot.getSlot(slot)
            )

            consumers.forEach { it.accept(enchantmentEffectsData) }
            onConsume(enchantmentEffectsData)
            if (enchantmentEffectsData.cancelEffects.isTrue) {
                continue
            }
            val matchesSlot = entry.key.value().matchingSlot(slot)
            if (enchantmentEffectsData.forceSlot.isFalse && !matchesSlot) {
                continue
            }

            val context = context(enchantmentEffectsData)
            for (effect in enchantmentEffects) {
                if (enchantmentEffectsData.cancelEffects.isTrue) {
                    continue
                }
                if (enchantmentEffectsData.forceSlot.isFalse && !matchesSlot) {
                    continue
                }
                if (enchantmentEffectsData.forceEffects.isFalse && !effect.matches(context)) {
                    continue
                }
                visitor.invoke(enchantmentEffectsData, entry.key, effect)
            }
        }
    }

    fun <T : ConditionalEffect<*>> net.minecraft.world.entity.LivingEntity.runIterationOnEnchantmentConditionalEffects(
        component: DataComponentType<List<T>>,
        context: (EnchantmentEffectsInSlotData) -> LootContext,
        consumers: List<Consumer<EnchantmentEffectsInSlotData>>,
        onConsume: (EnchantmentEffectsInSlotData) -> Unit = {},
        visitor: (EnchantmentEffectsInSlotData, Holder<Enchantment>, EquipmentSlot,  T) -> Unit
    ) {
        for (slot in EquipmentSlot.VALUES) {
            val itemStack = this.getItemBySlot(slot)
            itemStack.runIterationOnEnchantmentConditionalEffects(
                component = component,
                slot = slot,
                context = context,
                consumers = consumers,
                onConsume = onConsume
            ) { data, holder, effect ->
                visitor.invoke(data, holder, slot, effect)
            }
        }
    }

    fun <T : TargetedConditionalEffect<*>> net.minecraft.world.entity.LivingEntity.runIterationOnEnchantmentTargetedConditionalEffects(
        component: DataComponentType<List<T>>,
        context: (EnchantmentEffectsInSlotData) -> LootContext,
        consumers: List<Consumer<EnchantmentEffectsInSlotData>>,
        onConsume: (EnchantmentEffectsInSlotData) -> Unit = {},
        visitor: (EnchantmentEffectsInSlotData, Holder<Enchantment>, EquipmentSlot, T) -> Unit
    ) {
        for (slot in EquipmentSlot.VALUES) {
            val itemStack = this.getItemBySlot(slot)
            itemStack.runIterationOnEnchantmentTargetedConditionalEffects(
                component = component,
                slot = slot,
                context = context,
                consumers = consumers,
                onConsume = onConsume
            ) { data, holder, effect ->
                visitor.invoke(data, holder, slot, effect)
            }
        }
    }
}

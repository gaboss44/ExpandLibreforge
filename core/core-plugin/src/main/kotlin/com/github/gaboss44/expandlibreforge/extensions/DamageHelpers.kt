@file:Suppress("DEPRECATION", "UnstableApiUsage")

package com.github.gaboss44.expandlibreforge.extensions

import com.github.gaboss44.expandlibreforge.events.DamageSourceAwareEvent
import com.github.gaboss44.expandlibreforge.events.entity.EntityDamageArmorAbsorbReformulationEvent
import com.github.gaboss44.expandlibreforge.events.entity.EntityDamageMagicAbsorbReformulationEvent
import com.github.gaboss44.expandlibreforge.events.entity.EntityEnchantmentArmorEffectivenessEffectsEvent
import com.github.gaboss44.expandlibreforge.events.entity.EntityEnchantmentDamageProtectionEffectsEvent
import com.github.gaboss44.expandlibreforge.util.FieldUtils
import com.google.common.base.Function
import org.apache.commons.lang3.mutable.MutableFloat
import org.bukkit.damage.DamageSource
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Witch
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.tag.DamageTypeTags
import java.util.EnumMap
import java.util.function.BiFunction
import kotlin.collections.iterator

private val damageModifierFunctionsField = FieldUtils.getField(EntityDamageEvent::class.java, "modifierFunctions")

@Suppress("UNCHECKED_CAST")
fun EntityDamageEvent.getDamageModifierFunctions() : EnumMap<EntityDamageEvent.DamageModifier, Function<in Double, Double>>? {
    return try {
        val field = damageModifierFunctionsField?.get(this) ?: return null
        field as EnumMap<EntityDamageEvent.DamageModifier, Function<in Double, Double>>
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun getDamageModifier(string: String?): EntityDamageEvent.DamageModifier? {
    if (string == null) return null
    return try {
        EntityDamageEvent.DamageModifier.valueOf(string.lowercase())
    } catch (_: Exception) {
        null
    }
}

fun String?.toDamageModifier() = getDamageModifier(this)

fun EntityDamageEvent.recalculateDamageModifiers() {
    val functions = this.getDamageModifierFunctions() ?: return
    this.recalculateDamageModifiers(functions)
}

fun EntityDamageEvent.recalculateDamageModifiers(
    functions: Map<EntityDamageEvent.DamageModifier, Function<in Double, Double>>
) {
    var remaining = this.damage

    for (type in EntityDamageEvent.DamageModifier.entries) {
        if (!this.isApplicable(type) || type == EntityDamageEvent.DamageModifier.BASE) {
            continue
        }
        val function = functions[type] ?: continue
        val damage = function.apply(remaining)
        this.setDamage(type, damage)
        remaining += damage
    }
}

fun applyDefaultDamageProtectionFormula(damage: Float, protection: Float): Float {
    val f = protection.coerceIn(0.0f, 20.0f)
    return damage * (1.0F - f / 25.0F)
}

fun applyEventfulDamageProtectionModifier(entity: LivingEntity, source: DamageSource, protection: Float): Float {
    var protection = protection.coerceAtLeast(0.0f)
    MutableFloat(protection).let { mutable ->
        EnchantmentHelpers.modifyDamageProtection(mutable, entity.world, entity, source) { data ->
            val event = EntityEnchantmentDamageProtectionEffectsEvent(entity, data, mutable.value, protection, source)
            event.callEvent()
            mutable.value = event.protection
        }
        protection = mutable.value
    }
    return protection
}

fun LivingEntity.getDamageAfterMagicAbsorb(
    damage: Float,
    source: DamageSource,
    damageProtectionModifier: (LivingEntity, DamageSource, Float) -> Float,
    damageProtectionFormula: (Float, Float) -> Float
): Float {
    if (damage <= 0.0f) return 0.0f
    if (DamageTypeTags.BYPASSES_ENCHANTMENTS.isTagged(source.damageType)) return damage
    val protection = damageProtectionModifier(this, source, 0.0f).coerceAtLeast(0.0f)
    return damageProtectionFormula(damage, protection).coerceIn(0.0f, damage)
}

fun Witch.getDamageAfterMagicAbsorb(
    damage: Float,
    source: DamageSource,
    damageProtectionModifier: (LivingEntity, DamageSource, Float) -> Float,
    damageProtectionFormula: (Float, Float) -> Float
): Float {
    var damage = (this as LivingEntity).getDamageAfterMagicAbsorb(damage, source, damageProtectionModifier, damageProtectionFormula)

    if (source.causingEntity == this) {
        // damage *= 0.0f
        return 0.0f
    }

    if (DamageTypeTags.WITCH_RESISTANT_TO.isTagged(source.damageType)) {
        damage *= 0.15f
    }

    return damage
}

fun LivingEntity.getSpecificLivingDamageAfterMagicAbsorb(
    damage: Float,
    source: DamageSource,
    damageProtectionModifier: (LivingEntity, DamageSource, Float) -> Float,
    damageProtectionFormula: (Float, Float) -> Float
): Float {
    return if (this is Witch) {
        this.getDamageAfterMagicAbsorb(damage, source, damageProtectionModifier, damageProtectionFormula)
    } else {
        this.getDamageAfterMagicAbsorb(damage, source, damageProtectionModifier, damageProtectionFormula)
    }
}


fun EntityDamageEvent.callDamageMagicAbsorbReformulationEvent(): Boolean {
    val event = this.handleDamageMagicAbsorbReformulationEvent()
    return if (event == null) {
        false
    } else {
        !event.isCancelled
    }
}

fun EntityDamageEvent.handleDamageMagicAbsorbReformulationEvent(): EntityDamageMagicAbsorbReformulationEvent? {
    if (!this.isApplicable(EntityDamageEvent.DamageModifier.MAGIC)) return null
    val entity = this.entity as? LivingEntity ?: return null
    val functions = this.getDamageModifierFunctions() ?: return null

    val event = EntityDamageMagicAbsorbReformulationEvent(
        entity, this,
        ::applyEventfulDamageProtectionModifier,
        ::applyDefaultDamageProtectionFormula
    )
    if (!event.callEvent()) {
        return event
    }

    if (!this.isApplicable(EntityDamageEvent.DamageModifier.MAGIC)) {
        event.isCancelled = true
        return event
    }

    val damageProtectionModifier = event.damageProtectionModifier
    val damageProtectionFormula = event.damageProtectionFormula

    functions[EntityDamageEvent.DamageModifier.MAGIC] = Function { mod ->
        val damage = entity.getSpecificLivingDamageAfterMagicAbsorb(
            mod.toFloat(), damageSource,
            damageProtectionModifier,
            damageProtectionFormula
        )

        return@Function - (mod - damage)
    }

    return event
}

fun applyDefaultArmorEffectivenessFormula(damage: Float, effectiveness: Float): Float {
    if (damage <= 0.0f) return 0.0f
    val factor = 1.0f - effectiveness.coerceIn(0.0f, 1.0f)
    return damage * factor
}

fun applyDefaultArmorEffectivenessGetter(damage: Float, armor: Float, armorToughness: Float): Float {
    val damage = damage.coerceAtLeast(0.0f)
    val armor = armor.coerceAtLeast(0.0f)
    val armorToughness = armorToughness.coerceAtLeast(0.0f)
    val f = 2.0f + armorToughness / 4.0f
    val f1 = (armor - damage / f).coerceIn(armor * 0.2f, 20.0f)
    val f2 = f1 / 25.0f
    return f2
}

fun applyEventfulArmorEffectivenessModifier(entity: LivingEntity, source: DamageSource, effectiveness: Float): Float {
    var effectiveness = effectiveness.coerceIn(0.0f, 1.0f)
    val attacker = source.causingEntity ?: return effectiveness
    var weapon: ItemStack? = null
    if (attacker is Player) weapon = attacker.currentPlayerAttackData?.weapon
    if (weapon == null) weapon = source.weapon
    if (weapon == null) return effectiveness
    MutableFloat(effectiveness).let { mutable ->
        EnchantmentHelpers.modifyArmorEffectiveness(mutable, entity.world, entity, weapon, source) { data ->
            val event = EntityEnchantmentArmorEffectivenessEffectsEvent(entity, data, weapon, mutable.value, effectiveness, source)
            event.callEvent()
            mutable.value = event.effectiveness
        }
        effectiveness = mutable.value
    }
    return effectiveness
}

fun LivingEntity.getDamageAfterArmorAbsorb(
    damage: Float,
    source: DamageSource,
    armorEffectivenessGetter: (Float, Float, Float) -> Float,
    armorEffectivenessModifier: (LivingEntity, DamageSource, Float) -> Float,
    armorEffectivenessFormula: (Float, Float) -> Float
): Float {
    if (damage <= 0.0f) return 0.0f
    if (DamageTypeTags.BYPASSES_ARMOR.isTagged(source.damageType)) return damage
    val armor = this.armor.toFloat().coerceAtLeast(0.0f)
    val armorToughness = this.armorToughness.toFloat().coerceAtLeast(0.0f)
    var armorEffectiveness = armorEffectivenessGetter(damage, armor, armorToughness).coerceIn(0.0f, 1.0f)
    armorEffectiveness = armorEffectivenessModifier(this, source, armorEffectiveness).coerceIn(0.0f, 1.0f)
    return armorEffectivenessFormula(damage, armorEffectiveness).coerceIn(0.0f, damage)
}

fun EntityDamageEvent.callDamageArmorAbsorbReformulationEvent(): Boolean {
    val event = this.handleDamageArmorAbsorbReformulationEvent()
    return if (event == null) {
        false
    } else {
        !event.isCancelled
    }
}

fun EntityDamageEvent.handleDamageArmorAbsorbReformulationEvent(): EntityDamageArmorAbsorbReformulationEvent? {
    if (!this.isApplicable(EntityDamageEvent.DamageModifier.ARMOR)) return null
    val entity = this.entity as? LivingEntity ?: return null
    val functions = this.getDamageModifierFunctions() ?: return null

    val event = EntityDamageArmorAbsorbReformulationEvent(
        entity, this,
        ::applyDefaultArmorEffectivenessGetter,
        ::applyEventfulArmorEffectivenessModifier,
        ::applyDefaultArmorEffectivenessFormula
    )
    if (!event.callEvent()) {
        return event
    }

    if (!this.isApplicable(EntityDamageEvent.DamageModifier.ARMOR)) {
        event.isCancelled = true
        return event
    }

    val armorEffectivenessGetter = event.armorEffectivenessGetter
    val armorEffectivenessModifier = event.armorEffectivenessModifier
    val armorEffectivenessFormula = event.armorEffectivenessFormula

    functions[EntityDamageEvent.DamageModifier.ARMOR] = Function { mod ->
        val damage = entity.getDamageAfterArmorAbsorb(
            mod.toFloat(), damageSource,
            armorEffectivenessGetter,
            armorEffectivenessModifier,
            armorEffectivenessFormula
        )

        return@Function -(mod - damage)
    }

    return event
}

private val damageModifierReformulationCallers = mapOf(
    EntityDamageEvent.DamageModifier.ARMOR to { event: EntityDamageEvent -> event.callDamageArmorAbsorbReformulationEvent() },
    EntityDamageEvent.DamageModifier.MAGIC to { event: EntityDamageEvent -> event.callDamageMagicAbsorbReformulationEvent() }
)

fun EntityDamageEvent.reformulateDamageModifiers(
    skipRecalculation: Boolean,
    modifiers: List<EntityDamageEvent.DamageModifier>
) {
    var flag = false
    modifiers.mapNotNull {
        damageModifierReformulationCallers[it]
    }.forEach {
        if (it(this)) flag = true
    }
    if (!skipRecalculation && flag) {
        this.recalculateDamageModifiers()
    }
}

fun EntityDamageEvent.reformulateDamageModifiers(
    recalculate: Boolean,
    vararg modifiers: EntityDamageEvent.DamageModifier
) = this.reformulateDamageModifiers(recalculate, modifiers.toList())

fun EntityDamageEvent.reformulateDamageModifiers(
    modifiers: List<EntityDamageEvent.DamageModifier>
) = this.reformulateDamageModifiers(true, modifiers)

fun EntityDamageEvent.reformulateDamageModifiers(
    vararg modifiers: EntityDamageEvent.DamageModifier
) = this.reformulateDamageModifiers(true, modifiers.toList())

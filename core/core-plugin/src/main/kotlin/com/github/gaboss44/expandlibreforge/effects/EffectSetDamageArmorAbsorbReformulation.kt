package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.events.entity.EntityDamageArmorAbsorbReformulationEvent
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.NumberUtils
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.toPlaceholderContext
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.damage.DamageSource
import org.bukkit.entity.LivingEntity

@Suppress("UnstableApiUsage")
object EffectSetDamageArmorAbsorbReformulation : Effect<NoCompileData>("set_damage_armor_absorb_reformulation") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityDamageArmorAbsorbReformulationEvent ?: return false

        val placeholderContext = config.toPlaceholderContext(data)

        config.getSubsectionOrNull("armor_effectiveness_getter")?.let { sub ->
            val damageFunction = sub.getStringOrNull("damage")?.let { expr ->
                { damage: Float ->
                    val damageStr = damage.toString()
                    NumberUtils.evaluateExpression(expr
                        .replace("%damage%", damageStr),
                        placeholderContext
                    ).toFloat()
                }
            }
            val armorFunction = sub.getStringOrNull("armor")?.let { expr ->
                { armor: Float ->
                    val armorStr = armor.toString()
                    NumberUtils.evaluateExpression(expr
                        .replace("%armor%", armorStr),
                        placeholderContext
                    ).toFloat()
                }
            }
            val armorToughnessFunction = sub.getStringOrNull("armor_toughness")?.let { expr ->
                { armorToughness: Float ->
                    val armorToughnessStr = armorToughness.toString()
                    NumberUtils.evaluateExpression(expr
                        .replace("%armor_toughness%", armorToughnessStr)
                        .replace("%toughness%", armorToughnessStr),
                        placeholderContext
                    ).toFloat()
                }
            }
            val oldGetterFunction = event.armorEffectivenessGetter
            val getterFunction = sub.getStringOrNull("output")?.let { expr ->
                { damage: Float, armor: Float, armorToughness: Float ->
                    val damageStr = damage.toString()
                    val armorStr = armor.toString()
                    val armorToughnessStr = armorToughness.toString()
                    val outputStr = oldGetterFunction(damage, armor, armorToughness).toString()
                    NumberUtils.evaluateExpression(expr
                        .replace("%damage%", damageStr)
                        .replace("%armor%", armorStr)
                        .replace("%armor_toughness%", armorToughnessStr)
                        .replace("%toughness%", armorToughnessStr)
                        .replace("%output%", outputStr),
                        placeholderContext
                    ).toFloat()
                }
            }
            event.armorEffectivenessGetter = { damage: Float, armor: Float, armorToughness: Float ->
                val damage = damageFunction?.invoke(damage) ?: damage
                val armor = armorFunction?.invoke(armor) ?: armor
                val armorToughness = armorToughnessFunction?.invoke(armorToughness) ?: armorToughness
                if (getterFunction != null) {
                    getterFunction(damage, armor, armorToughness)
                } else {
                    oldGetterFunction(damage, armor, armorToughness)
                }
            }
        }

        config.getSubsectionOrNull("armor_effectiveness_modifier")?.let { sub ->
            val armorEffectivenessFunction = sub.getStringOrNull("armor_effectiveness")?.let { expr ->
                { armorEffectiveness: Float ->
                    val armorEffectivenessStr = armorEffectiveness.toString()
                    NumberUtils.evaluateExpression(expr
                        .replace("%armor_effectiveness%", armorEffectivenessStr)
                        .replace("%effectiveness%", armorEffectivenessStr),
                        placeholderContext
                    ).toFloat()
                }
            }
            val oldModifierFunction = event.armorEffectivenessModifier
            val modifierFunction = sub.getStringOrNull("output")?.let { expr ->
                { entity: LivingEntity, source: DamageSource, armorEffectiveness: Float ->
                    val armorEffectivenessStr = armorEffectiveness.toString()
                    val outputStr = oldModifierFunction(entity, source, armorEffectiveness).toString()
                    NumberUtils.evaluateExpression(expr
                        .replace("%armor_effectiveness%", armorEffectivenessStr)
                        .replace("%effectiveness%", armorEffectivenessStr)
                        .replace("%output%", outputStr),
                        placeholderContext
                    ).toFloat()
                }
            }
            event.armorEffectivenessModifier = { entity: LivingEntity, source: DamageSource, armorEffectiveness: Float ->
                val armorEffectiveness = armorEffectivenessFunction?.invoke(armorEffectiveness) ?: armorEffectiveness
                if (modifierFunction != null) {
                    modifierFunction(entity, source, armorEffectiveness)
                } else {
                    oldModifierFunction(entity, source, armorEffectiveness)
                }
            }
        }

        config.getSubsectionOrNull("armor_effectiveness_formula")?.let { sub ->
            val damageFunction = sub.getStringOrNull("damage")?.let { expr ->
                { damage: Float ->
                    val damageStr = damage.toString()
                    NumberUtils.evaluateExpression(expr
                        .replace("%damage%", damageStr),
                        placeholderContext
                    ).toFloat()
                }
            }
            val armorEffectivenessFunction = sub.getStringOrNull("armor_effectiveness")?.let { expr ->
                { armorEffectiveness: Float ->
                    val armorEffectivenessStr = armorEffectiveness.toString()
                    NumberUtils.evaluateExpression(expr
                        .replace("%armor_effectiveness%", armorEffectivenessStr)
                        .replace("%effectiveness%", armorEffectivenessStr),
                        placeholderContext
                    ).toFloat()
                }
            }
            val oldFormulaFunction = event.armorEffectivenessFormula
            val formulaFunction = sub.getStringOrNull("output")?.let { expr ->
                { damage: Float, armorEffectiveness: Float ->
                    val damageStr = damage.toString()
                    val armorEffectivenessStr = armorEffectiveness.toString()
                    val outputStr = oldFormulaFunction(damage, armorEffectiveness).toString()
                    NumberUtils.evaluateExpression(expr
                        .replace("%damage%", damageStr)
                        .replace("%armor_effectiveness%", armorEffectivenessStr)
                        .replace("%effectiveness%", armorEffectivenessStr)
                        .replace("%output%", outputStr),
                        placeholderContext
                    ).toFloat()
                }
            }
            event.armorEffectivenessFormula = { damage: Float, armorEffectiveness: Float ->
                val damage = damageFunction?.invoke(damage) ?: damage
                val armorEffectiveness = armorEffectivenessFunction?.invoke(armorEffectiveness) ?: armorEffectiveness
                if (formulaFunction != null) {
                    formulaFunction(damage, armorEffectiveness)
                } else {
                    oldFormulaFunction(damage, armorEffectiveness)
                }
            }
        }

        return true
    }
}

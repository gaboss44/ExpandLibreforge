package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.events.entity.EntityDamageMagicAbsorbReformulationEvent
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
object EffectSetDamageMagicAbsorbReformulation : Effect<NoCompileData>("set_damage_magic_absorb_reformulation") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityDamageMagicAbsorbReformulationEvent ?: return false

        val placeholderContext = config.toPlaceholderContext(data)

        config.getSubsectionOrNull("damage_protection_modifier")?.let { sub ->
            val damageProtectionFunction = sub.getStringOrNull("damage_protection")?.let { expr ->
                { damageProtection: Float ->
                    val damageProtectionStr = damageProtection.toString()
                    NumberUtils.evaluateExpression(expr
                        .replace("%damage_protection%", damageProtectionStr)
                        .replace("%protection%", damageProtectionStr),
                        placeholderContext
                    ).toFloat()
                }
            }
            val oldModifierFunction = event.damageProtectionModifier
            val modifierFunction = sub.getStringOrNull("output")?.let { expr ->
                { entity: LivingEntity, source: DamageSource, damageProtection: Float ->
                    val damageProtectionStr = damageProtection.toString()
                    val outputStr = oldModifierFunction(entity, source, damageProtection).toString()
                    NumberUtils.evaluateExpression(expr
                        .replace("%damage_protection%", damageProtectionStr)
                        .replace("%protection%", damageProtectionStr)
                        .replace("%output%", outputStr),
                        placeholderContext
                    ).toFloat()
                }
            }
            event.damageProtectionModifier = { entity: LivingEntity, source: DamageSource, damageProtection: Float ->
                val damageProtection = damageProtectionFunction?.invoke(damageProtection) ?: damageProtection
                if (modifierFunction != null) {
                    modifierFunction(entity, source, damageProtection)
                } else {
                    oldModifierFunction(entity, source, damageProtection)
                }
            }
        }

        config.getSubsectionOrNull("damage_protection_formula")?.let { sub ->
            val damageFunction = sub.getStringOrNull("damage")?.let { expr ->
                { damage: Float ->
                    val damageStr = damage.toString()
                    NumberUtils.evaluateExpression(expr
                        .replace("%damage%", damageStr),
                        placeholderContext
                    ).toFloat()
                }
            }
            val damageProtectionFunction = sub.getStringOrNull("damage_protection")?.let { expr ->
                { damageProtection: Float ->
                    val damageProtectionStr = damageProtection.toString()
                    NumberUtils.evaluateExpression(expr
                        .replace("%damage_protection%", damageProtectionStr)
                        .replace("%protection%", damageProtectionStr),
                        placeholderContext
                    ).toFloat()
                }
            }
            val oldFormulaFunction = event.damageProtectionFormula
            val formulaFunction = sub.getStringOrNull("output")?.let { expr ->
                { damage: Float, damageProtection: Float ->
                    val damageStr = damage.toString()
                    val damageProtectionStr = damageProtection.toString()
                    val outputStr = oldFormulaFunction(damage, damageProtection).toString()
                    NumberUtils.evaluateExpression(expr
                        .replace("%damage%", damageStr)
                        .replace("%damage_protection%", damageProtectionStr)
                        .replace("%protection%", damageProtectionStr)
                        .replace("%output%", outputStr),
                        placeholderContext
                    ).toFloat()
                }
            }
            event.damageProtectionFormula = { damage: Float, damageProtection: Float ->
                val damage = damageFunction?.invoke(damage) ?: damage
                val damageProtection = damageProtectionFunction?.invoke(damageProtection) ?: damageProtection
                if (formulaFunction != null) {
                    formulaFunction(damage, damageProtection)
                } else {
                    oldFormulaFunction(damage, damageProtection)
                }
            }
        }

        return true
    }
}
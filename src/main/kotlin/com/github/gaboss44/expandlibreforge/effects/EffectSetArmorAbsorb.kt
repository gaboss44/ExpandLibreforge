package com.github.gaboss44.expandlibreforge.effects

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.NumberUtils
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.toPlaceholderContext
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import io.papermc.paper.event.entity.EntityDamageArmorAbsorbEvent

object EffectSetArmorAbsorb : Effect<NoCompileData>("set_armor_absorb") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityDamageArmorAbsorbEvent ?: return false
        val damageStr = event.damage.toString()
        val overrideDamageStr = event.overrideDamage.toString()
        val armorValueStr = event.armorPoints.toString()
        val armorToughnessValueStr = event.armorToughness.toString()
        val overrideArmorValueStr = event.overrideArmorPoints.toString()
        val overrideArmorToughnessValueStr = event.overrideArmorToughness.toString()
        val placeholderContext = config.toPlaceholderContext(data)

        config.getStringOrNull("override_damage")?.let { expr ->
            event.overrideDamage = NumberUtils.evaluateExpression(expr
                .replace("%damage%", damageStr)
                .replace("%d%", damageStr)
                .replace("%override_damage%", overrideDamageStr)
                .replace("%ovd%", overrideDamageStr)
                .replace("%armor_points%", armorValueStr)
                .replace("%ar%", armorValueStr)
                .replace("%override_armor_points%", overrideArmorValueStr)
                .replace("%oar%", overrideArmorValueStr)
                .replace("%armor_toughness%", armorToughnessValueStr)
                .replace("%at%", armorToughnessValueStr)
                .replace("%override_armor_toughness%", overrideArmorToughnessValueStr)
                .replace("%oat%", overrideArmorToughnessValueStr),
                placeholderContext).toFloat()
        }

        config.getStringOrNull("armor_points")?.let { expr ->
            event.overrideArmorPoints = NumberUtils.evaluateExpression(expr
                .replace("%damage%", damageStr)
                .replace("%d%", damageStr)
                .replace("%override_damage%", overrideDamageStr)
                .replace("%ovd%", overrideDamageStr)
                .replace("%armor_points%", armorValueStr)
                .replace("%ar%", armorValueStr)
                .replace("%override_armor_points%", overrideArmorValueStr)
                .replace("%oar%", overrideArmorValueStr)
                .replace("%armor_toughness%", armorToughnessValueStr)
                .replace("%at%", armorToughnessValueStr)
                .replace("%override_armor_toughness%", overrideArmorToughnessValueStr)
                .replace("%oat%", overrideArmorToughnessValueStr),
                placeholderContext).toInt()
        }

        config.getStringOrNull("armor_toughness")?.let { expr ->
            event.overrideArmorToughness = NumberUtils.evaluateExpression(expr
                .replace("%damage%", damageStr)
                .replace("%d%", damageStr)
                .replace("%override_damage%", overrideDamageStr)
                .replace("%ovd%", overrideDamageStr)
                .replace("%armor_points%", armorValueStr)
                .replace("%ar%", armorValueStr)
                .replace("%override_armor_points%", overrideArmorValueStr)
                .replace("%oar%", overrideArmorValueStr)
                .replace("%armor_toughness%", armorToughnessValueStr)
                .replace("%at%", armorToughnessValueStr)
                .replace("%override_armor_toughness%", overrideArmorToughnessValueStr)
                .replace("%oat%", overrideArmorToughnessValueStr),
                placeholderContext).toFloat()
        }

        config.getStringOrNull("formula")?.let { expr ->
            val oldFormula = event.formula
            event.formula = EntityDamageArmorAbsorbEvent.Formula { d: Float, a: Int, t: Float ->
                val outputStr = oldFormula.calculate(d, a, t).toString()
                NumberUtils.evaluateExpression(expr
                    .replace("%output%", outputStr)
                    .replace("%o%", outputStr)
                    .replace("%damage%", damageStr)
                    .replace("%d%", damageStr)
                    .replace("%override_damage%", overrideDamageStr)
                    .replace("%ovd%", overrideDamageStr)
                    .replace("%armor_points%", armorValueStr)
                    .replace("%ar%", armorValueStr)
                    .replace("%override_armor_points%", overrideArmorValueStr)
                    .replace("%oar%", overrideArmorValueStr)
                    .replace("%armor_toughness%", armorToughnessValueStr)
                    .replace("%at%", armorToughnessValueStr)
                    .replace("%override_armor_toughness%", overrideArmorToughnessValueStr)
                    .replace("%oat%", overrideArmorToughnessValueStr),
                    placeholderContext).toFloat()
            }
        }

        return true
    }
}

package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.extensions.getDamageModifierFunctions
import com.github.gaboss44.expandlibreforge.extensions.recalculateDamageModifiers
import com.github.gaboss44.expandlibreforge.util.getDamageModifierEnum
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.NumberUtils
import com.willfp.eco.util.toNiceString
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.toPlaceholderContext
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.event.entity.EntityDamageEvent

object EffectSetDamageModifier : Effect<NoCompileData>("set_damage_modifier") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override val arguments = arguments {
        require("modifier", "You must specify a damage modifier")
        require("value", "You must specify a value")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityDamageEvent ?: return false
        val type = config.getDamageModifierEnum("modifier") ?: return false
        if (!event.isApplicable(type) || type == EntityDamageEvent.DamageModifier.BASE) return false
        val expression = config.getString("value")
        val context = config.toPlaceholderContext(data)
        event.getDamageModifierFunctions()?.let { modifierFunctions ->
            val modifierFunction = modifierFunctions[type]!!
            modifierFunctions.put(type) { input: Double ->
                val output = modifierFunction.apply(input)
                val inputStr = input.toNiceString()
                val outputStr = output.toNiceString()
                val damageStr = event.damage.toNiceString()
                val modifierStr = event.getDamage(type).toNiceString()
                var expression = expression
                expression = expression.replace("%input%", inputStr)
                expression = expression.replace("%i%", inputStr)
                expression = expression.replace("%output%", outputStr)
                expression = expression.replace("%o%", outputStr)
                expression = expression.replace("%damage%", damageStr)
                expression = expression.replace("%d%", damageStr)
                expression = expression.replace("%modifier%", modifierStr)
                expression = expression.replace("%m%", modifierStr)
                NumberUtils.evaluateExpression(expression, context)
            }
            event.recalculateDamageModifiers(modifierFunctions)
        }
        return true
    }
}

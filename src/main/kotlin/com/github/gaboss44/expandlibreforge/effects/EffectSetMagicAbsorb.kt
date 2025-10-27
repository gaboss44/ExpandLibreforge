package com.github.gaboss44.expandlibreforge.effects

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.NumberUtils
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.toPlaceholderContext
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import io.papermc.paper.event.entity.EntityDamageMagicAbsorbEvent

object EffectSetMagicAbsorb : Effect<NoCompileData>("set_magic_absorb") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityDamageMagicAbsorbEvent ?: return false
        val damageStr = event.damage.toString()
        val overrideDamageStr = event.overrideDamage.toString()
        val magicProtectionStr = event.magicProtection.toString()
        val overrideMagicProtectionStr = event.overrideMagicProtection.toString()
        val placeholderContext = config.toPlaceholderContext(data)

        config.getStringOrNull("override_damage")?.let { expr ->
            event.overrideDamage = NumberUtils.evaluateExpression(expr
                .replace("%damage%", damageStr)
                .replace("%d%", damageStr)
                .replace("%override_damage%", overrideDamageStr)
                .replace("%ovd%", overrideDamageStr)
                .replace("%magic_protection%", magicProtectionStr)
                .replace("%mp%", magicProtectionStr)
                .replace("%override_magic_protection%", overrideMagicProtectionStr)
                .replace("%omp%", overrideMagicProtectionStr),
                placeholderContext).toFloat()
        }

        config.getStringOrNull("magic_protection")?.let { expr ->
            event.overrideMagicProtection = NumberUtils.evaluateExpression(expr
                .replace("%damage%", damageStr)
                .replace("%d%", damageStr)
                .replace("%override_damage%", overrideDamageStr)
                .replace("%ovd%", overrideDamageStr)
                .replace("%magic_protection%", magicProtectionStr)
                .replace("%mp%", magicProtectionStr)
                .replace("%override_magic_protection%", overrideMagicProtectionStr)
                .replace("%omp%", overrideMagicProtectionStr),
                placeholderContext).toFloat()
        }

        config.getStringOrNull("formula")?.let { expr ->
            val oldFormula = event.formula
            event.formula = EntityDamageMagicAbsorbEvent.Formula { d: Float, m: Float ->
                val damageInputStr = d.toString()
                val magicProtectionInputStr = m.toString()
                val outputStr = oldFormula.calculate(d, m).toString()
                NumberUtils.evaluateExpression(expr
                    .replace("%damage_input%", damageInputStr)
                    .replace("%di%", damageInputStr)
                    .replace("%magic_protection_input%", magicProtectionInputStr)
                    .replace("%mpi%", magicProtectionInputStr)
                    .replace("%output%", outputStr)
                    .replace("%o%", outputStr)
                    .replace("%damage%", damageStr)
                    .replace("%d%", damageStr)
                    .replace("%override_damage%", overrideDamageStr)
                    .replace("%ovd%", overrideDamageStr)
                    .replace("%magic_protection%", magicProtectionStr)
                    .replace("%mp%", magicProtectionStr)
                    .replace("%override_magic_protection%", overrideMagicProtectionStr)
                    .replace("%omp%", overrideMagicProtectionStr),
                    placeholderContext).toFloat()
            }
        }

        return true
    }
}

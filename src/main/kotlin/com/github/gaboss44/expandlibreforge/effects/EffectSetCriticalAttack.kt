package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.util.floatHalfMaxValue
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.NumberUtils
import com.willfp.eco.util.toNiceString
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.toPlaceholderContext
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import io.papermc.paper.event.player.PlayerAttackEntityCriticalCheckEvent

object EffectSetCriticalAttack : Effect<NoCompileData>("set_critical_attack") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? PlayerAttackEntityCriticalCheckEvent ?: return false

        val critical = config.getBoolOrNull("critical")

        if (critical != null) {
            event.isCritical = critical
        }

        val multiplierStr = config.getStringOrNull("multiplier")

        if (multiplierStr != null && multiplierStr.isNotBlank()) {
            val baseDamageStr = event.baseDamage.toNiceString()
            val enchantedDamageStr = event.enchantedDamage.toNiceString()
            val criticalMultiplier = event.criticalMultiplier.toNiceString()
            val originalCriticalMultiplierStr = event.originalCriticalMultiplier.toNiceString()
            val finalDamageStr = event.finalDamage.toNiceString()

            val expr = multiplierStr
                .replace("%base_damage%", baseDamageStr)
                .replace("%bd%", baseDamageStr)
                .replace("%enchanted_damage%", enchantedDamageStr)
                .replace("%ed%", enchantedDamageStr)
                .replace("%critical_multiplier%", criticalMultiplier)
                .replace("%cm%", criticalMultiplier)
                .replace("%original_critical_multiplier%", originalCriticalMultiplierStr)
                .replace("%ocm%", originalCriticalMultiplierStr)
                .replace("%final_damage%", finalDamageStr)
                .replace("%fd%", finalDamageStr)

            event.criticalMultiplier = NumberUtils.evaluateExpression(
                expr, config.toPlaceholderContext(data)
            ).toFloat().coerceIn(0.0f, floatHalfMaxValue)
        }

        return true
    }
}
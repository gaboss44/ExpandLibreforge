package com.github.gaboss44.expandlibreforge.effects

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.NumberUtils
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.toPlaceholderContext
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter

object EffectSetAttackStrengthTicker : Effect<NoCompileData>("set_attack_strength_ticker") {
    override val parameters = setOf(
        TriggerParameter.PLAYER
    )

    override val arguments = arguments {
        require("strength_ticks", "You must set the new strength ticks")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val player = data.player ?: return false

        val attackStrengthTickerStr = player.attackStrengthTicker.toString()
        player.attackStrengthTicker = NumberUtils.evaluateExpression(
            (config.getStringOrNull("strength_ticks") ?: return false)
                .replace("%strength_ticks%", attackStrengthTickerStr)
                .replace("%st%", attackStrengthTickerStr),
            config.toPlaceholderContext(data)).toInt()

        return true
    }
}
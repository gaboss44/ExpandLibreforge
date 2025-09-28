package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.features.combo.ComboManager
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.getFormattedString
import com.willfp.libreforge.getIntFromExpression
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter

object EffectExtendCombo : Effect<NoCompileData>("extend_combo") {
    override val parameters = setOf(
        TriggerParameter.PLAYER
    )

    override val arguments = arguments {
        require("name", "You must specify the combo name")
        require("duration", "You must specify the combo duration")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val playerId = data.player?.uniqueId ?: return false
        val comboName = config.getFormattedString("name", data)
        val duration = config.getIntFromExpression("duration", data)
        val reset = config.getBool("reset")
        ComboManager.extendCombo(playerId, comboName, duration, reset)
        return true
    }
}
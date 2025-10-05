package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.features.combo.ComboManager
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.getFormattedString
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter

object EffectEndCombo : Effect<NoCompileData>("end_combo") {
    override val parameters = setOf(
        TriggerParameter.PLAYER
    )

    override val arguments = arguments {
        require("name", "You must specify the combo name")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val player = data.player ?: return false
        val comboName = config.getFormattedString("name", data)
        val updateEffects = config.getBool("update_effects")
        ComboManager.endCombo(player, comboName, updateEffects)
        return true
    }
}
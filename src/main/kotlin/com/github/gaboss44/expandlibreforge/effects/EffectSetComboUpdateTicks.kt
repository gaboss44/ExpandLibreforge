package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.events.combo.ComboTickEvent
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.getIntFromExpression
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter

object EffectSetComboUpdateTicks : Effect<NoCompileData>("set_combo_update_ticks") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override val arguments = arguments {
        require("value", "You must specify the value to set")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? ComboTickEvent ?: return false
        val value = config.getIntFromExpression("value", data)
        event.updateTicks = value
        return true
    }
}
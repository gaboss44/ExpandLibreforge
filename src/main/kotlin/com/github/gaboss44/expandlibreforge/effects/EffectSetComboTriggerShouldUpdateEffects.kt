package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.events.combo.ComboEvent
import com.github.gaboss44.expandlibreforge.events.combo.PlayerComboEvent
import com.github.gaboss44.expandlibreforge.features.combo.Combo
import com.github.gaboss44.expandlibreforge.util.ComboTriggeredEffect
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.ConfigArguments
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.triggers.TriggerData

object EffectSetComboTriggerShouldUpdateEffects : ComboTriggeredEffect<NoCompileData>("set_combo_trigger_should_update_effects") {
    override val arguments = arguments {
        require("value", "You must specify a value")
    }
    override fun onTrigger(
        config: Config,
        data: TriggerData,
        compileData: NoCompileData,
        event: ComboEvent,
        combo: Combo
    ): Boolean {
        if (event !is PlayerComboEvent) return false
        event.shouldUpdateEffects = config.getBool("update_effects")
        return true
    }
}
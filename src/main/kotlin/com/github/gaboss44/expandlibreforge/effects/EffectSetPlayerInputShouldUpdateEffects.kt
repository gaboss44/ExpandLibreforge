package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.events.input.PlayerInputUpdateEvent
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter

object EffectSetPlayerInputShouldUpdateEffects : Effect<NoCompileData>("set_player_input_should_update_effects") {
    override val arguments = arguments {
        require("value", "You must set a value (true/false)")
    }

    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? PlayerInputUpdateEvent ?: return false
        event.setShouldUpdateEffects(config.getBool("value"))
        return true
    }
}
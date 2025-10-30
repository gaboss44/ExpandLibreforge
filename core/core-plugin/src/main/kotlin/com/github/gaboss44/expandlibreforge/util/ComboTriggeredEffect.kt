package com.github.gaboss44.expandlibreforge.util

import com.github.gaboss44.expandlibreforge.events.combo.ComboEvent
import com.github.gaboss44.expandlibreforge.features.combo.Combo
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter

abstract class ComboTriggeredEffect<T>(id: String) : Effect<T>(id) {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    final override fun onTrigger(config: Config, data: TriggerData, compileData: T): Boolean {
        val event = data.event as? ComboEvent ?: return false
        val combo = event.combo

        if (!config.check(combo, data)) return false

        return onTrigger(config, data, compileData, event, combo)
    }

    open fun onTrigger(
        config: Config,
        data: TriggerData,
        compileData: T,
        event: ComboEvent,
        combo: Combo
    ) : Boolean = true
}
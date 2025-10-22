package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.util.getEventResult
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import io.papermc.paper.event.entity.EntityEnchantedItemLocationChangedEffectsEvent

object EffectSetEnchantedItemLocationChangedEffectsContextResult : Effect<NoCompileData>("set_enchanted_item_location_changed_effects_context_result") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override val arguments = arguments {
        require("context_result", "You must specify a new context result")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityEnchantedItemLocationChangedEffectsEvent ?: return false
        val newResult = getEventResult(config.getString("context_result")) ?: return false
        event.contextResult = newResult
        return true
    }
}
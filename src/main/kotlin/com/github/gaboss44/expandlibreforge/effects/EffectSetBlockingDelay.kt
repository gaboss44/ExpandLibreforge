package com.github.gaboss44.expandlibreforge.effects

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.NumberUtils
import com.willfp.eco.util.toNiceString
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.toPlaceholderContext
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import io.papermc.paper.event.entity.EntityBlockingDelayCheckEvent

object EffectSetBlockingDelay : Effect<NoCompileData>("set_blocking_delay") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override val arguments = arguments {
        require("blocking_delay", "You must set a blocking delay value")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityBlockingDelayCheckEvent ?: return false
        val current = event.blockingDelay.toNiceString()
        val expr = config.getStringOrNull("blocking_delay")
            ?.replace("%blocking_delay%", current)
            ?.replace("%bd%", current)
            ?: return false
        event.blockingDelay = NumberUtils.evaluateExpression(
            expr,
            config.toPlaceholderContext(data)
        ).coerceAtLeast(0.0).toInt()
        return true
    }
}
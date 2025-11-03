package com.github.gaboss44.expandlibreforge.effects

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.NumberUtils
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.toPlaceholderContext
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.event.entity.EntityExhaustionEvent

object EffectSetExhaustion : Effect<NoCompileData>("set_exhaustion") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override val arguments = arguments {
        require("exhaustion", "You must set a exhaustion value")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityExhaustionEvent ?: return false
        val exhaustionStr = event.exhaustion.toString()

        val newExhaustion = (config.getStringOrNull("exhaustion") ?: return false)
            .replace("%exhaustion%", exhaustionStr)
            .replace("%exh%", exhaustionStr)
            .let { NumberUtils.evaluateExpression(it, config.toPlaceholderContext(data)) }

        event.exhaustion = newExhaustion.toFloat()
        return true
    }
}
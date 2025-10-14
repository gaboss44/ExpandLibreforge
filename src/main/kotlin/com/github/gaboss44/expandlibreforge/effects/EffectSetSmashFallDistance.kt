package com.github.gaboss44.expandlibreforge.effects

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.NumberUtils
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.toPlaceholderContext
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import io.papermc.paper.event.entity.EntitySmashAttackFallDistanceEvent

object EffectSetSmashFallDistance : Effect<NoCompileData>("set_smash_fall_distance") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override val arguments = arguments {
        require("fall_distance", "You must specify the new smash check result")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? EntitySmashAttackFallDistanceEvent ?: return false
        val expr = config.getStringOrNull("fall_distance") ?: return false
        val fallDistanceStr = event.fallDistance.toString()
        val originalFallDistanceStr = event.originalFallDistance.toString()
        event.fallDistance = NumberUtils.evaluateExpression(
            expr
                .replace("%fall_distance%", fallDistanceStr)
                .replace("%fd%", fallDistanceStr)
                .replace("original_fall_distance", originalFallDistanceStr)
                .replace("%ofd%", originalFallDistanceStr),
            config.toPlaceholderContext(data)
        )

        return true
    }
}
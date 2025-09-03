package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.utils.StartRiptideAttackMethod
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.getIntFromExpression
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter

object EffectDoRiptideSpin : Effect<NoCompileData>("do_riptide_spin") {
    override val parameters = setOf(
        TriggerParameter.PLAYER
    )

    override val arguments = arguments {
        require("duration", "You must specify the duration")
        require("strength", "You must specify the strength")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val player = data.player ?: return false

        val duration = config.getIntFromExpression("duration", data)
        if (duration <= 0) return false

        val strength = config.getDoubleFromExpression("strength", data)
        if (strength <= 0) return false

        return StartRiptideAttackMethod.invoke(player, duration, strength.toFloat())
    }
}
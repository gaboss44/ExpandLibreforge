package com.github.gaboss44.expandlibreforge.effects

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter

object EffectMultiplyProjectileVelocity : Effect<NoCompileData>("multiply_projectile_velocity") {
    override val parameters = setOf(
        TriggerParameter.PROJECTILE
    )

    override val arguments = arguments {
        require("multiplier", "You must specify the velocity multiplier.")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val projectile = data.projectile ?: return false
        val multiplier = config.getDoubleFromExpression("multiplier", data)
        val velocity = projectile.velocity.multiply(multiplier)
        projectile.velocity = velocity
        return true
    }
}
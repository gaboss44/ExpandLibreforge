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
import io.papermc.paper.event.player.PlayerShieldDisableEvent

object EffectSetShieldDisableCooldown : Effect<NoCompileData>("set_shield_disable_cooldown") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override val arguments = arguments {
        require("shield_cooldown", "You must set a shield cooldown value")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? PlayerShieldDisableEvent ?: return false
        val current = event.cooldown.toNiceString()
        val expr = config.getStringOrNull("shield_cooldown")
            ?.replace("%shield_cooldown%", current)
            ?.replace("%sc%", current)
            ?: return false
        event.cooldown = NumberUtils.evaluateExpression(
            expr,
            config.toPlaceholderContext(data)
        ).coerceAtLeast(0.0).toInt()
        return true
    }
}
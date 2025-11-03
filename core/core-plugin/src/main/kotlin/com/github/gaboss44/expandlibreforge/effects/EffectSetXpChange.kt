package com.github.gaboss44.expandlibreforge.effects

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.getIntFromExpression
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.event.player.PlayerExpChangeEvent

object EffectSetXpChange : Effect<NoCompileData>("set_xp_change") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override val arguments = arguments {
        require("value", "You must specify the value.")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? PlayerExpChangeEvent ?: return false
        val value = config.getIntFromExpression("value", data)
        event.amount = value
        return true
    }
}
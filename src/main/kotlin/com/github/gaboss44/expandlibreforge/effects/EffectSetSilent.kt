package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.util.EntityTarget
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.triggers.TriggerData

object EffectSetSilent : Effect<NoCompileData>("set_silent") {

    override val isPermanent = false

    override val arguments = arguments {
        require("value", "You must specify the value.")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val target = EntityTarget[config.getString("target")] ?: EntityTarget.PLAYER
        val entity = target.getEntity(data) ?: return false
        val value = config.getBoolOrNull("value") ?: true
        entity.isSilent = value
        return true
    }

}
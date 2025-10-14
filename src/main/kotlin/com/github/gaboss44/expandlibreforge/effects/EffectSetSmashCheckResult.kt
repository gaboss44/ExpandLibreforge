package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.util.getEventResult
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import io.papermc.paper.event.entity.EntityCanSmashAttackCheckEvent

object EffectSetSmashCheckResult : Effect<NoCompileData>("set_smash_check_result") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override val arguments = arguments {
        require("result", "You must specify the new smash check result")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityCanSmashAttackCheckEvent ?: return false
        val newResult = getEventResult(config.getStringOrNull("result")) ?: return false
        event.result = newResult
        return true
    }
}
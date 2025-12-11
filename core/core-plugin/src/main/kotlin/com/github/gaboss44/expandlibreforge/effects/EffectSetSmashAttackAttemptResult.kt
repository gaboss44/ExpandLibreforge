package com.github.gaboss44.expandlibreforge.effects

import com.github.gaboss44.expandlibreforge.events.entity.AttemptSmashAttackEvent
import com.github.gaboss44.expandlibreforge.util.getEventResult
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter

object EffectSetSmashAttackAttemptResult : Effect<NoCompileData>("set_smash_attack_attempt_result") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    override val arguments = arguments {
        require("smash_attack_attempt_result", "You must specify the new smash attack attempt result")
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val event = data.event as? AttemptSmashAttackEvent ?: return false
        val newResult = getEventResult(config.getStringOrNull("smash_attack_attempt_result")) ?: return false
        event.result = newResult
        return true
    }
}

package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import io.papermc.paper.event.entity.EntityAttemptSmashAttackEvent

object FilterSmashAttemptIsOriginallySuccessful : Filter<NoCompileData, Boolean>("smash_attempt_is_originally_successful") {
    override fun getValue(config: Config, data: TriggerData?, key: String): Boolean {
        return config.getBool(key)
    }

    override fun isMet(data: TriggerData, value: Boolean, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityAttemptSmashAttackEvent ?: return true
        return event.originalResult == value
    }
}
package com.github.gaboss44.expandlibreforge.filters

import com.github.gaboss44.expandlibreforge.events.entity.AttemptSmashAttackEvent
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.containsIgnoreCase
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.getStrings
import com.willfp.libreforge.triggers.TriggerData

object FilterIgnoreSmashAttackAttemptResultIfPresent : Filter<NoCompileData, Collection<String>>("ignore_smash_attack_attempt_result_if_present") {
    override fun getValue(config: Config, data: TriggerData?, key: String): Collection<String> {
        return config.getStrings(key, key)
    }

    override fun isMet(data: TriggerData, value: Collection<String>, compileData: NoCompileData): Boolean {
        val event = data.event as? AttemptSmashAttackEvent ?: return true
        return !value.containsIgnoreCase(event.result.name)
    }
}
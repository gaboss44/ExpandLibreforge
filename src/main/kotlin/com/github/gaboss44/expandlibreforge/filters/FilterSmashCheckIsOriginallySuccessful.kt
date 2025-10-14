package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import io.papermc.paper.event.entity.EntityCanSmashAttackCheckEvent

object FilterSmashCheckIsOriginallySuccessful : Filter<NoCompileData, Boolean>("smash_check_is_originally_successful") {
    override fun getValue(config: Config, data: TriggerData?, key: String): Boolean {
        return config.getBool(key)
    }

    override fun isMet(data: TriggerData, value: Boolean, compileData: NoCompileData): Boolean {
        val event = data.event as? EntityCanSmashAttackCheckEvent ?: return true
        return event.originalResult == value
    }
}

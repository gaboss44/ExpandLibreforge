package com.github.gaboss44.expandlibreforge.filters

import com.github.gaboss44.expandlibreforge.extensions.tryGetDamageSource
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData

@Suppress("UnstableApiUsage")
object FilterDamageSourceIsIndirect : Filter<NoCompileData, Boolean>("damage_source_is_indirect") {
    override fun getValue(
        config: Config,
        data: TriggerData?,
        key: String
    ): Boolean {
        return config.getBool(key)
    }

    override fun isMet(
        data: TriggerData,
        value: Boolean,
        compileData: NoCompileData
    ): Boolean {
        val event = data.event ?: return true
        val source = event.tryGetDamageSource() ?: return true
        return source.isIndirect == value
    }

}
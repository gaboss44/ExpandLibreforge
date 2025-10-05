package com.github.gaboss44.expandlibreforge.filters

import com.github.gaboss44.expandlibreforge.events.combo.ComboEvent
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData

object FilterComboIsPresent : Filter<NoCompileData, Boolean>("combo_is_present") {
    override fun getValue(config: Config, data: TriggerData?, key: String): Boolean {
        return config.getBool(key)
    }

    override fun isMet(data: TriggerData, value: Boolean, compileData: NoCompileData): Boolean {
        return data.event as? ComboEvent != null == value
    }
}
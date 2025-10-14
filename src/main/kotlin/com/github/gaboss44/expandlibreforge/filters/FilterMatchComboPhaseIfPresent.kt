package com.github.gaboss44.expandlibreforge.filters

import com.github.gaboss44.expandlibreforge.events.combo.ComboEvent
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.containsIgnoreCase
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.getFormattedStrings
import com.willfp.libreforge.triggers.TriggerData

object FilterMatchComboPhaseIfPresent : Filter<NoCompileData, Collection<String>>("match_combo_phase_if_present") {
    override fun getValue(config: Config, data: TriggerData?, key: String): Collection<String> {
        return config.getFormattedStrings(key, data)
    }

    override fun isMet(data: TriggerData, value: Collection<String>, compileData: NoCompileData): Boolean {
        val event = data.event as? ComboEvent ?: return true
        return value.containsIgnoreCase(event.combo.phase.name)
    }
}
package com.github.gaboss44.expandlibreforge.filters

import com.github.gaboss44.expandlibreforge.features.combo.ComboManager
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.getFormattedStrings
import com.willfp.libreforge.triggers.TriggerData

object FilterMatchComboIfAny : Filter<NoCompileData, Collection<String>>("match_combo_if_any") {
    override fun getValue(config: Config, data: TriggerData?, key: String): Collection<String> {
        return config.getFormattedStrings(key, data)
    }

    override fun isMet(data: TriggerData, value: Collection<String>, compileData: NoCompileData): Boolean {
        val player = data.player ?: return true
        val combos = ComboManager.getCombos(player.uniqueId) ?: return true
        if (combos.isEmpty()) return true
        return value.any { combos.containsKey(it) }
    }
}
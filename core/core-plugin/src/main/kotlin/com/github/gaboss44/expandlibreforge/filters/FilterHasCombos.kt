package com.github.gaboss44.expandlibreforge.filters

import com.github.gaboss44.expandlibreforge.features.combo.Combo
import com.github.gaboss44.expandlibreforge.util.ComboFilter
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.triggers.TriggerData

object FilterHasCombos : ComboFilter<NoCompileData>("has_combos") {
    override fun isMet(
        data: TriggerData,
        values: List<Config>,
        compileData: NoCompileData,
        matches: List<Pair<Config, Combo>>
    ): Boolean {
        return matches.isNotEmpty()
    }
}
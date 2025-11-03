package com.github.gaboss44.expandlibreforge.util

import com.github.gaboss44.expandlibreforge.features.combo.Combo
import com.github.gaboss44.expandlibreforge.features.combo.ComboManager
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.getFormattedString
import com.willfp.libreforge.getIntFromExpression
import com.willfp.libreforge.triggers.TriggerData

abstract class ComboFilter<T>(id: String) : Filter<T, List<Config>>(id) {
    final override fun getValue(config: Config, data: TriggerData?, key: String): List<Config> {
        return config.getSubsections(key)
    }

    final override fun isMet(data: TriggerData, value: List<Config>, compileData: T): Boolean {
        val player = data.player ?: return false

        fun Config.toCombo(): Pair<Config, Combo>? {
            val name = this.getFormattedString("name", data)
            val combo = ComboManager.getCombo(player.uniqueId, name) ?: return null

            return if (this.check(combo, data)) this to combo else null
        }

        return this.isMet(
            data,
            value,
            compileData,
            value.mapNotNull{it.toCombo()}
        )
    }

    open fun isMet(
        data: TriggerData,
        values: List<Config>,
        compileData: T,
        matches: List<Pair<Config, Combo>>
    ) = true
}

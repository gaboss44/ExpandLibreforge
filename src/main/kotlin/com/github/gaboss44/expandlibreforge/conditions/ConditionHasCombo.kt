package com.github.gaboss44.expandlibreforge.conditions

import com.github.gaboss44.expandlibreforge.features.combo.ComboManager
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.placeholder.context.PlaceholderContext
import com.willfp.libreforge.Dispatcher
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.ProvidedHolder
import com.willfp.libreforge.arguments
import com.willfp.libreforge.conditions.Condition
import com.willfp.libreforge.get
import org.bukkit.entity.Player

object ConditionHasCombo : Condition<NoCompileData>("has_combo") {
    override val arguments = arguments {
        require("name", "You must specify the name of the combo")
    }

    override fun isMet(
        dispatcher: Dispatcher<*>,
        config: Config,
        holder: ProvidedHolder,
        compileData: NoCompileData
    ): Boolean {
        val player = dispatcher.get<Player>() ?: return false
        val name = config.getFormattedString("name", PlaceholderContext(player))
        val combo = ComboManager.getCombo(player.uniqueId, name) ?: return false

        fun Config.getOptionalInt(key: String): Int? =
            if (this.has(key)) this.getIntFromExpression(key, player) else null

        config.getOptionalInt("count_equals")?.let { if (combo.count != it) return false }
        config.getOptionalInt("count_above")?.let { if (combo.count <= it) return false }
        config.getOptionalInt("count_below")?.let { if (combo.count >= it) return false }

        config.getOptionalInt("remaining_ticks_equals")?.let { if (combo.remainingTicks != it) return false }
        config.getOptionalInt("remaining_ticks_above")?.let { if (combo.remainingTicks <= it) return false }
        config.getOptionalInt("remaining_ticks_below")?.let { if (combo.remainingTicks >= it) return false }

        return true
    }
}

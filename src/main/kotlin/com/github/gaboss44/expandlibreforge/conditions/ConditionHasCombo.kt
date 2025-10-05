package com.github.gaboss44.expandlibreforge.conditions

import com.github.gaboss44.expandlibreforge.features.combo.ComboManager
import com.github.gaboss44.expandlibreforge.util.check
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

        return config.check(combo, player)
    }
}

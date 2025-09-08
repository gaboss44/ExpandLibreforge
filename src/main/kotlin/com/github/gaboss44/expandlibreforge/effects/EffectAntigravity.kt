package com.github.gaboss44.expandlibreforge.effects

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.map.listMap
import com.willfp.libreforge.Dispatcher
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.ProvidedHolder
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.effects.Identifiers
import com.willfp.libreforge.get
import org.bukkit.entity.Player
import java.util.UUID

object EffectAntigravity : Effect<NoCompileData>("antigravity") {
    override val shouldReload = false

    private val players = listMap<UUID, UUID>()

    override fun onEnable(
        dispatcher: Dispatcher<*>,
        config: Config,
        identifiers: Identifiers,
        holder: ProvidedHolder,
        compileData: NoCompileData
    ) {
        val player = dispatcher.get<Player>() ?: return

        players[player.uniqueId].add(identifiers.uuid)
        player.setGravity(players[player.uniqueId].isEmpty())
    }

    override fun onDisable(dispatcher: Dispatcher<*>, identifiers: Identifiers, holder: ProvidedHolder) {
        val player = dispatcher.get<Player>() ?: return

        players[player.uniqueId].remove(identifiers.uuid)
        player.setGravity(players[player.uniqueId].isEmpty())
    }
}
package com.github.gaboss44.expandlibreforge.listeners

import com.destroystokyo.paper.event.server.ServerTickEndEvent
import com.github.gaboss44.expandlibreforge.features.combo.ComboManager.tickAllCombos
import com.github.gaboss44.expandlibreforge.features.shield.ShieldingPlayers.checkAllShields
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.updateEffects
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

object ServerTickListener : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onTickEnd(event: ServerTickEndEvent) {
        val updatedCombos = tickAllCombos(event)
        val toggledShields = checkAllShields(event)

        for (playerId in updatedCombos union toggledShields) {
            Bukkit.getPlayer(playerId)?.toDispatcher()?.updateEffects()
        }
    }
}
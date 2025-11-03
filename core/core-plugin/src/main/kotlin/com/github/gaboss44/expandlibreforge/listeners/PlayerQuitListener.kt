package com.github.gaboss44.expandlibreforge.listeners

import com.github.gaboss44.expandlibreforge.features.shield.ShieldingPlayers
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerQuitListener : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        ShieldingPlayers.remove(player.uniqueId)
    }
}
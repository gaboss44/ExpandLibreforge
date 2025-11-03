package com.github.gaboss44.expandlibreforge.listeners

import com.github.gaboss44.expandlibreforge.features.shield.ShieldingPlayers
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerJoinListener : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (player.isBlocking) {
            ShieldingPlayers.add(player.uniqueId)
        }
    }
}
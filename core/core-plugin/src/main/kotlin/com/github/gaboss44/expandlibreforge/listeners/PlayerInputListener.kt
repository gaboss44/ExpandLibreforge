package com.github.gaboss44.expandlibreforge.listeners

import com.github.gaboss44.expandlibreforge.events.input.PlayerInputUpdateEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInputEvent

object PlayerInputListener : Listener {

    @EventHandler
    fun onPlayerInput(event: PlayerInputEvent) {
        PlayerInputUpdateEvent(event, false).callEvent()
    }
}
package com.github.gaboss44.expandlibreforge.triggers

import com.github.gaboss44.expandlibreforge.events.input.PlayerInputUpdateEvent
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.updateEffects
import org.bukkit.event.EventHandler

object TriggerPlayerInput : Trigger("player_input") {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.EVENT
    )

    @EventHandler(ignoreCancelled = true)
    fun handle(event: PlayerInputUpdateEvent) {
        val player = event.player
        val dispatcher = player.toDispatcher()
        this.dispatch(
            dispatcher,
            TriggerData(
                player = player,
                event = event
            )
        )
        if (event.shouldUpdateEffects()) {
            dispatcher.updateEffects()
        }
    }
}
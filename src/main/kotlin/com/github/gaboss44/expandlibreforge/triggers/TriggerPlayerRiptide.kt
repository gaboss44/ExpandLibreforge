package com.github.gaboss44.expandlibreforge.triggers

import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerRiptideEvent

object TriggerPlayerRiptide : Trigger("player_riptide") {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.EVENT,
        TriggerParameter.ITEM
    )

    @EventHandler(ignoreCancelled = true)
    fun handle(event: PlayerRiptideEvent) {
        val player = event.player
        this.dispatch(
            player.toDispatcher(),
            TriggerData(
                player = player,
                event = event,
                item = event.item
            )
        )
    }
}
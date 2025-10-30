package com.github.gaboss44.expandlibreforge.triggers

import com.destroystokyo.paper.event.server.ServerTickStartEvent
import com.willfp.libreforge.GlobalDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.event.EventHandler

object TriggerServerTickStart : Trigger("server_tick_start") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    @EventHandler(ignoreCancelled = true)
    fun handle(event: ServerTickStartEvent) {
        this.dispatch(
            GlobalDispatcher,
            TriggerData(
                event = event,
                value = event.tickNumber.toDouble()
            )
        )
    }
}
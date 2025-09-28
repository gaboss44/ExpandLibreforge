package com.github.gaboss44.expandlibreforge.triggers

import com.destroystokyo.paper.event.server.ServerTickEndEvent
import com.willfp.libreforge.GlobalDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.event.EventHandler

object TriggerServerTickEnd : Trigger("server_tick_end") {
    override val parameters = setOf(
        TriggerParameter.EVENT
    )

    @EventHandler(ignoreCancelled = true)
    fun handle(event: ServerTickEndEvent) {
        this.dispatch(
            GlobalDispatcher,
            TriggerData(
                event = event,
                value = event.tickNumber.toDouble()
            )
        )
    }
}
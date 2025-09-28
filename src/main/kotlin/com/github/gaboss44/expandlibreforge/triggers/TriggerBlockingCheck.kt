package com.github.gaboss44.expandlibreforge.triggers

import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import io.papermc.paper.event.entity.EntityBlockingDelayCheckEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler

object TriggerBlockingCheck : Trigger("blocking_delay_check") {

    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.EVENT,
        TriggerParameter.VALUE,
        TriggerParameter.ALT_VALUE
    )

    @EventHandler(ignoreCancelled = true)
    fun handle(event: EntityBlockingDelayCheckEvent) {
        val entity = event.entity

        this.dispatch(
            entity.toDispatcher(),
            TriggerData(
                player = entity as? Player,
                event = event,
                item = event.itemStack,
                value = event.originalBlockingDelay.toDouble(),
                altValue = event.ticksUsing.toDouble()
            )
        )
    }
}
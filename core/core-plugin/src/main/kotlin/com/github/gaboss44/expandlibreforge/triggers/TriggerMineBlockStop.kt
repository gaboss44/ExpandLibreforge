package com.github.gaboss44.expandlibreforge.triggers

import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockDamageAbortEvent

object TriggerMineBlockStop : Trigger("mine_block_stop") {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.BLOCK,
        TriggerParameter.LOCATION,
        TriggerParameter.EVENT,
        TriggerParameter.ITEM
    )

    @EventHandler(ignoreCancelled = true)
    fun handle(event: BlockDamageAbortEvent) {
        val player = event.player
        val block = event.block

        this.dispatch(
            player.toDispatcher(),
            TriggerData(
                player = player,
                block = block,
                location = block.location,
                event = event,
                item = player.inventory.itemInMainHand
            )
        )
    }
}
package com.github.gaboss44.expandlibreforge.triggers

import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import io.papermc.paper.event.entity.EntityMoveEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerMoveEvent
import kotlin.math.sqrt

sealed class TriggerLocalMove(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.LOCATION,
        TriggerParameter.VELOCITY,
        TriggerParameter.EVENT,
        TriggerParameter.ITEM,
        TriggerParameter.VALUE,
        TriggerParameter.ALT_VALUE
    )

    fun handle(event: EntityMoveEvent) {
        if (event.from.world != event.to.world) return

        val offset = event.to.toVector().subtract(event.from.toVector())

        val xz = sqrt(offset.x * offset.x + offset.z * offset.z)

        val y = offset.y

        this.dispatch(
            event.entity.toDispatcher(),
            TriggerData(
                event = event,
                location = event.entity.location,
                velocity = event.entity.velocity,
                item = event.entity.equipment?.itemInMainHand,
                value = xz,
                altValue = y
            )
        )
    }

    fun handle(event: PlayerMoveEvent) {
        if (event.from.world != event.to.world) return

        val offset = event.to.toVector().subtract(event.from.toVector())

        val xz = sqrt(offset.x * offset.x + offset.z * offset.z)

        val y = offset.y

        this.dispatch(
            event.player.toDispatcher(),
            TriggerData(
                player = event.player,
                event = event,
                location = event.player.location,
                velocity = event.player.velocity,
                item = event.player.equipment.itemInMainHand,
                value = xz,
                altValue = y
            )
        )
    }

    companion object {
        fun registerAllInto(category: Triggers) {
            category.register(HighestPriority)
            category.register(HighPriority)
            category.register(Default)
            category.register(NormalPriority)
            category.register(LowPriority)
            category.register(LowestPriority)
            category.register(Monitor)
        }
    }

    object HighestPriority : TriggerLocalMove("local_move_highest_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        fun onMove(event: EntityMoveEvent) = handle(event)

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        fun onMove(event: PlayerMoveEvent) = handle(event)
    }

    object HighPriority : TriggerLocalMove("local_move_high_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
        fun onMove(event: EntityMoveEvent) = handle(event)

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
        fun onMove(event: PlayerMoveEvent) = handle(event)
    }

    object NormalPriority : TriggerLocalMove("local_move_normal_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
        fun onMove(event: EntityMoveEvent) = handle(event)

        @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
        fun onMove(event: PlayerMoveEvent) = handle(event)
    }

    object LowPriority : TriggerLocalMove("local_move_low_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
        fun onMove(event: EntityMoveEvent) = handle(event)

        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
        fun onMove(event: PlayerMoveEvent) = handle(event)
    }

    object LowestPriority : TriggerLocalMove("local_move_lowest_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
        fun onMove(event: EntityMoveEvent) = handle(event)

        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
        fun onMove(event: PlayerMoveEvent) = handle(event)
    }

    object Monitor : TriggerLocalMove("local_move_monitor") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        fun onMove(event: EntityMoveEvent) = handle(event)

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        fun onMove(event: PlayerMoveEvent) = handle(event)
    }

    object Default : TriggerLocalMove("local_move") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
        fun onMove(event: EntityMoveEvent) = handle(event)

        @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
        fun onMove(event: PlayerMoveEvent) = handle(event)
    }
}
package com.github.gaboss44.expandlibreforge.triggers

import com.github.gaboss44.expandlibreforge.ExpandLibreforgePlugin
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.FluidCollisionMode
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent

class TriggerInteract(private val plugin: ExpandLibreforgePlugin) : Trigger("interact") {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM,
        TriggerParameter.EVENT,
        TriggerParameter.ITEM,
        TriggerParameter.BLOCK
    )

    @EventHandler(ignoreCancelled = true)
    fun handle(event: PlayerInteractEvent) {
        val player = event.player
        val world = player.location.world ?: return
        val result = player.rayTraceBlocks(
            plugin.configYml.getDoubleOrNull("raytrace-distance") ?: 5.0,
            FluidCollisionMode.NEVER
        )
        val entityResult = world.rayTraceEntities(
            player.eyeLocation,
            player.eyeLocation.direction, 50.0, 3.0
        ) { entity: Entity? -> entity is LivingEntity }
        val victim = entityResult?.hitEntity as? LivingEntity
        this.dispatch(
            player.toDispatcher(),
            TriggerData(
                player = player,
                victim = victim,
                event = event,
                item = event.item,
                block = event.clickedBlock ?: result?.hitBlock ?: victim?.location?.block
            )
        )
    }
}
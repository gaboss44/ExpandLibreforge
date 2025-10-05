package com.github.gaboss44.expandlibreforge.triggers

import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.ProjectileHitEvent

sealed class TriggerProjectileHits(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM,
        TriggerParameter.PROJECTILE,
        TriggerParameter.LOCATION,
        TriggerParameter.BLOCK,
        TriggerParameter.EVENT,
        TriggerParameter.VELOCITY
    )

    fun handle(event: ProjectileHitEvent) {
        this.dispatch(
            event.entity.toDispatcher(),
            TriggerData(
                event = event,
                player = event.entity.shooter as? Player,
                victim = event.hitEntity as? LivingEntity,
                block = event.hitBlock,
                projectile = event.entity,
                velocity = event.entity.velocity,
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

    object HighestPriority : TriggerProjectileHits("projectile_hits_highest_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        fun onProjectileHit(event: ProjectileHitEvent) = handle(event)
    }

    object HighPriority : TriggerProjectileHits("projectile_hits_high_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
        fun onProjectileHit(event: ProjectileHitEvent) = handle(event)
    }

    object NormalPriority : TriggerProjectileHits("projectile_hits_normal_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
        fun onProjectileHit(event: ProjectileHitEvent) = handle(event)
    }

    object LowPriority : TriggerProjectileHits("projectile_hits_low_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
        fun onProjectileHit(event: ProjectileHitEvent) = handle(event)
    }

    object LowestPriority : TriggerProjectileHits("projectile_hits_lowest_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
        fun onProjectileHit(event: ProjectileHitEvent) = handle(event)
    }

    object Monitor : TriggerProjectileHits("projectile_hits_monitor") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        fun onProjectileHit(event: ProjectileHitEvent) = handle(event)
    }

    object Default : TriggerProjectileHits("projectile_hits") {
        @EventHandler(ignoreCancelled = true)
        fun onProjectileHit(event: ProjectileHitEvent) = handle(event)
    }
}
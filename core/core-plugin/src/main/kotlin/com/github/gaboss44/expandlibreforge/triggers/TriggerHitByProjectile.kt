package com.github.gaboss44.expandlibreforge.triggers

import com.github.gaboss44.expandlibreforge.extensions.firedFromWeapon
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.ProjectileHitEvent

sealed class TriggerHitByProjectile(id: String) : Trigger(id) {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM,
        TriggerParameter.ITEM,
        TriggerParameter.PROJECTILE,
        TriggerParameter.LOCATION,
        TriggerParameter.BLOCK,
        TriggerParameter.EVENT,
        TriggerParameter.VELOCITY
    )

    fun handle(event: ProjectileHitEvent) {
        val entity = event.hitEntity as? LivingEntity ?: return
        val weapon = (event.entity as? AbstractArrow)?.firedFromWeapon
        this.dispatch(
            entity.toDispatcher(),
            TriggerData(
                event = event,
                player = entity as? Player,
                victim = event.entity.shooter as? LivingEntity,
                block = event.hitBlock,
                projectile = event.entity,
                item = weapon,
                location = event.entity.location,
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

    object HighestPriority : TriggerHitByProjectile("hit_by_projectile_highest_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        fun onProjectileHit(event: ProjectileHitEvent) = handle(event)
    }

    object HighPriority : TriggerHitByProjectile("hit_by_projectile_high_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
        fun onProjectileHit(event: ProjectileHitEvent) = handle(event)
    }

    object NormalPriority : TriggerHitByProjectile("hit_by_projectile_normal_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
        fun onProjectileHit(event: ProjectileHitEvent) = handle(event)
    }

    object LowPriority : TriggerHitByProjectile("hit_by_projectile_low_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
        fun onProjectileHit(event: ProjectileHitEvent) = handle(event)
    }

    object LowestPriority : TriggerHitByProjectile("hit_by_projectile_lowest_priority") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
        fun onProjectileHit(event: ProjectileHitEvent) = handle(event)
    }

    object Monitor : TriggerHitByProjectile("hit_by_projectile_monitor") {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        fun onProjectileHit(event: ProjectileHitEvent) = handle(event)
    }

    object Default : TriggerHitByProjectile("hit_by_projectile") {
        @EventHandler(ignoreCancelled = true)
        fun onProjectileHit(event: ProjectileHitEvent) = handle(event)
    }
}
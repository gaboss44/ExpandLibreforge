package com.github.gaboss44.expandlibreforge.events.entity

import io.papermc.paper.event.entity.EntityAttemptSmashAttackEvent
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityEvent
import org.bukkit.inventory.ItemStack

interface AttemptSmashAttackEvent {

    val event: EntityEvent

    val attacker: LivingEntity

    val target: LivingEntity

    val weapon: ItemStack

    val originalResult: Boolean

    var result: Event.Result

    fun callEvent(): Boolean

    companion object {

        @Suppress("UnstableApiUsage")
        fun createPaper(
            attacker: LivingEntity,
            target: LivingEntity,
            weapon: ItemStack,
            originalResult: Boolean
        ) = wrapPaper(EntityAttemptSmashAttackEvent(attacker, target, weapon, originalResult))

        fun wrapPaper(
            event: EntityAttemptSmashAttackEvent
        ): AttemptSmashAttackEvent = object : AttemptSmashAttackEvent by event {}
    }
}

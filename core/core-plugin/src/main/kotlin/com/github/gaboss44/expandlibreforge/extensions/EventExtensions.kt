@file:Suppress("UnstableApiUsage")
package com.github.gaboss44.expandlibreforge.extensions

import com.github.gaboss44.expandlibreforge.events.DamageSourceAwareEvent
import com.github.gaboss44.expandlibreforge.events.entity.AttemptSmashAttackEvent
import com.github.gaboss44.expandlibreforge.prerequisites.PrerequisiteHasSmashAttemptEvent
import io.papermc.paper.event.entity.EntityAttemptSmashAttackEvent
import org.bukkit.damage.DamageSource
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityEvent

fun EntityEvent.tryAsAttemptSmashAttackEvent() =
    if (this is AttemptSmashAttackEvent) {
        this
    } else if (PrerequisiteHasSmashAttemptEvent.isMet && this is EntityAttemptSmashAttackEvent) {
        AttemptSmashAttackEvent.wrapPaper(this)
    } else {
        null
    }

fun Event.tryGetDamageSource(): DamageSource? {
    return when(this) {
        is EntityDamageEvent -> this.damageSource
        is EntityDeathEvent -> this.damageSource
        is DamageSourceAwareEvent -> this.damageSource
        else -> null
    }
}
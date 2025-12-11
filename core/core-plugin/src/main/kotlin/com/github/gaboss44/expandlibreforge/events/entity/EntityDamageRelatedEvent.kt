package com.github.gaboss44.expandlibreforge.events.entity

import com.github.gaboss44.expandlibreforge.events.DamageSourceAwareEvent
import com.github.gaboss44.expandlibreforge.events.ParentalEvent
import org.bukkit.event.entity.EntityDamageEvent

@Suppress("UnstableApiUsage")
interface EntityDamageRelatedEvent : ParentalEvent, DamageSourceAwareEvent {

    override val parent: EntityDamageEvent

    override val damageSource get() = this.parent.damageSource
}
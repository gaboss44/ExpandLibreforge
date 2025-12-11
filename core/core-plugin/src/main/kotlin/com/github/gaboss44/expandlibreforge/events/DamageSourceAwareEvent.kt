package com.github.gaboss44.expandlibreforge.events

import org.bukkit.damage.DamageSource

@Suppress("UnstableApiUsage")
interface DamageSourceAwareEvent {

    val damageSource: DamageSource
}
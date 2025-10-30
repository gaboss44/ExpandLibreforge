package com.github.gaboss44.expandlibreforge.proxies

import org.bukkit.damage.DamageSource

@Suppress("UnstableApiUsage")
interface DamageSourceAccessorProxy {

    fun toCritical(
        source: DamageSource
    ): DamageSource
}
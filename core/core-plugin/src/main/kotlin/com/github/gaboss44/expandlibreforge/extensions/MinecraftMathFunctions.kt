package com.github.gaboss44.expandlibreforge.extensions

import com.github.gaboss44.expandlibreforge.proxies.MinecraftMathProxy

private lateinit var minecraftMath: MinecraftMathProxy

object MinecraftMathFunctions  {

    fun setProxyIfNeeded(proxy: MinecraftMathProxy) {
        if (!::minecraftMath.isInitialized) {
            minecraftMath = proxy
        }
    }
}

fun sin(value: Float) : Float {
    return minecraftMath.sin(value)
}

fun cos(value: Float) : Float {
    return minecraftMath.cos(value)
}
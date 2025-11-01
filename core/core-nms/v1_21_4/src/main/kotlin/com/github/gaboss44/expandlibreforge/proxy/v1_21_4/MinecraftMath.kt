package com.github.gaboss44.expandlibreforge.proxy.v1_21_4

import com.github.gaboss44.expandlibreforge.proxies.MinecraftMathProxy
import net.minecraft.util.Mth

class MinecraftMath : MinecraftMathProxy {
    override fun sin(value: Float): Float {
        return Mth.sin(value)
    }

    override fun cos(value: Float): Float {
        return Mth.cos(value)
    }
}
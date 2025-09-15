package com.github.gaboss44.expandlibreforge.util

import org.bukkit.SoundCategory

object SoundCategoryUtils {

    fun getCategoryOrElse(name: String?, default: SoundCategory?) : SoundCategory? {
        if (name == null) return default
        return try {
            SoundCategory.valueOf(name.uppercase())
        } catch (_: IllegalArgumentException) {
            default
        }
    }
}
package com.github.gaboss44.expandlibreforge.util

enum class Tristate : BooleanGetter {
    TRUE {
        override val booleanValue = true
        override fun getBooleanValue(value: Boolean) = true
        override fun getBooleanValue(supplier: () -> Boolean) = true
    },
    FALSE {
        override val booleanValue = false
        override fun getBooleanValue(value: Boolean) = false
        override fun getBooleanValue(supplier: () -> Boolean) = false
    },
    DEFAULT {
        override val booleanValue = false
        override fun getBooleanValue(value: Boolean) = value
        override fun getBooleanValue(supplier: () -> Boolean) = supplier()
    }
}

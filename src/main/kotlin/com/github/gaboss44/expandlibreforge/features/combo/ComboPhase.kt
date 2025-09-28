package com.github.gaboss44.expandlibreforge.features.combo

enum class ComboPhase(val lowerValue: String) {
    START("start"),
    IDLE("idle"),
    TICK("tick"),
    END("end");

    companion object {
        private val byLowerValue = entries.associateBy { it.lowerValue }

        operator fun get(lowerValue: String?) = byLowerValue[lowerValue?.lowercase()]
    }
}
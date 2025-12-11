package com.github.gaboss44.expandlibreforge.util

class MutableTristate(initial: Tristate = Tristate.DEFAULT) : BooleanGetter {

    private var state: Tristate = initial

    var value: Tristate
        get() = state
        set(v) { state = v }

    override val booleanValue: Boolean
        get() = state.booleanValue

    override fun getBooleanValue(value: Boolean): Boolean =
        state.getBooleanValue(value)

    override fun getBooleanValue(supplier: () -> Boolean): Boolean =
        state.getBooleanValue(supplier)

    fun setTrue() {
        state = Tristate.TRUE
    }

    fun setFalse() {
        state = Tristate.FALSE
    }

    fun setDefault() {
        state = Tristate.DEFAULT
    }

    fun isTrue(): Boolean = state == Tristate.TRUE
    fun isFalse(): Boolean = state == Tristate.FALSE
    fun isDefault(): Boolean = state == Tristate.DEFAULT

    fun toTristate(): Tristate = state

    override fun toString(): String = "MutableTristate($state)"

    companion object {

        fun newDefault() = MutableTristate(Tristate.DEFAULT)

        fun newTrue() = MutableTristate(Tristate.TRUE)

        fun newFalse() = MutableTristate(Tristate.FALSE)
    }
}

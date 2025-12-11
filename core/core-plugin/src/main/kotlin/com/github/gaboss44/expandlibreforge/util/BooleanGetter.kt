package com.github.gaboss44.expandlibreforge.util

import java.util.function.BooleanSupplier

interface BooleanGetter {

    val booleanValue: Boolean

    fun getBooleanValue(value: Boolean): Boolean

    fun getBooleanValue(supplier: () -> Boolean): Boolean

    fun getBooleanValue(supplier: BooleanSupplier) = this.getBooleanValue(supplier::getAsBoolean)
}
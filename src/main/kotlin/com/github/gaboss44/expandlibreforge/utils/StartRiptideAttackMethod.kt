package com.github.gaboss44.expandlibreforge.utils

import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Method

object StartRiptideAttackMethod {
    private var method: Method? = null

    init {
        method = try {
            HumanEntity::class.java.getMethod(
                "startRiptideAttack",
                Int::class.javaPrimitiveType,   // int
                Float::class.javaPrimitiveType, // float
                ItemStack::class.java           // ItemStack
            )
        } catch (_: NoSuchMethodException) {
            null
        }
    }

    fun invoke(human: HumanEntity, duration: Int, strength: Float, item: ItemStack? = null): Boolean {
        method?.let {
            try {
                it.invoke(human, duration, strength, item)
                return true
            } catch (_: Exception) {
                return false
            }
        }
        return false
    }
}

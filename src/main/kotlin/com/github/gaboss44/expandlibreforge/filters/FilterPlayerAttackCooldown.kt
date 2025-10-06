package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.triggers.TriggerData

sealed class FilterPlayerAttackCooldown(id: String) : Filter<NoCompileData, Double>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Double {
        return config.getDoubleFromExpression(key, data)
    }

    final override fun isMet(data: TriggerData, value: Double, compileData: NoCompileData): Boolean {
        val player = data.player ?: return true
        return compare(player.attackCooldown.toDouble(), value)
    }

    abstract fun compare(playerCooldown: Double, value: Double): Boolean

    companion object {
        fun registerAllInto(category: Filters) {
            category.register(Equals)
            category.register(AtLeast)
            category.register(AtMost)
            category.register(GreaterThan)
            category.register(LowerThan)
        }
    }

    object Equals : FilterPlayerAttackCooldown("player_attack_cooldown_equals") {
        override fun compare(playerCooldown: Double, value: Double): Boolean {
            return playerCooldown == value
        }
    }

    object AtLeast : FilterPlayerAttackCooldown("player_attack_cooldown_at_least") {
        override fun compare(playerCooldown: Double, value: Double): Boolean {
            return playerCooldown >= value
        }
    }

    object AtMost : FilterPlayerAttackCooldown("player_attack_cooldown_at_most") {
        override fun compare(playerCooldown: Double, value: Double): Boolean {
            return playerCooldown <= value
        }
    }

    object GreaterThan : FilterPlayerAttackCooldown("player_attack_cooldown_greater_than") {
        override fun compare(playerCooldown: Double, value: Double): Boolean {
            return playerCooldown > value
        }
    }

    object LowerThan : FilterPlayerAttackCooldown("player_attack_cooldown_lower_than") {
        override fun compare(playerCooldown: Double, value: Double): Boolean {
            return playerCooldown < value
        }
    }
}
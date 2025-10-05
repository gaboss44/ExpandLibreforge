package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.entity.HumanEntity

sealed class FilterVictimAttackCooldown(id: String) : Filter<NoCompileData, Double>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Double {
        return config.getDoubleFromExpression(key, data)
    }

    final override fun isMet(data: TriggerData, value: Double, compileData: NoCompileData): Boolean {
        val victim = data.victim as? HumanEntity ?: return true
        return compare(victim.attackCooldown.toDouble(), value)
    }

    abstract fun compare(victimCooldown: Double, value: Double): Boolean

    companion object {
        fun registerAllInto(category: Filters) {
            category.register(Equals)
            category.register(AtLeast)
            category.register(AtMost)
            category.register(GreaterThan)
            category.register(LowerThan)
        }
    }

    object Equals : FilterVictimAttackCooldown("victim_cooldown_equals") {
        override fun compare(victimCooldown: Double, value: Double): Boolean {
            return victimCooldown == value
        }
    }

    object AtLeast : FilterVictimAttackCooldown("victim_cooldown_at_least") {
        override fun compare(victimCooldown: Double, value: Double): Boolean {
            return victimCooldown >= value
        }
    }

    object AtMost : FilterVictimAttackCooldown("victim_cooldown_at_most") {
        override fun compare(victimCooldown: Double, value: Double): Boolean {
            return victimCooldown <= value
        }
    }

    object GreaterThan : FilterVictimAttackCooldown("victim_cooldown_greater_than") {
        override fun compare(victimCooldown: Double, value: Double): Boolean {
            return victimCooldown > value
        }
    }

    object LowerThan : FilterVictimAttackCooldown("victim_cooldown_lower_than") {
        override fun compare(victimCooldown: Double, value: Double): Boolean {
            return victimCooldown < value
        }
    }
}
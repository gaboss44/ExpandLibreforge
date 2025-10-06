package com.github.gaboss44.expandlibreforge.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.entity.Player

sealed class FilterVictimCooledAttackStrength(id: String) : Filter<NoCompileData, Config>(id) {

    final override fun getValue(config: Config, data: TriggerData?, key: String): Config {
        return config.getSubsection(key)
    }

    final override fun isMet(data: TriggerData, value: Config, compileData: NoCompileData): Boolean {
        val victim = data.victim as? Player ?: return true
        val cooledAttackStrength = victim.getCooledAttackStrength(value.getDoubleFromExpression("adjust_ticks", data).toFloat())
        return compare(cooledAttackStrength.toDouble(), value.getDoubleFromExpression("value", data))
    }

    abstract fun compare(cooledAttackStrength: Double, value: Double): Boolean

    companion object {
        fun registerAllInto(category: Filters) {
            category.register(Equals)
            category.register(AtLeast)
            category.register(AtMost)
            category.register(GreaterThan)
            category.register(LowerThan)
        }
    }

    object Equals : FilterVictimCooledAttackStrength("victim_cooled_attack_strength_equals") {
        override fun compare(cooledAttackStrength: Double, value: Double): Boolean {
            return cooledAttackStrength == value
        }
    }

    object AtLeast : FilterVictimCooledAttackStrength("victim_cooled_attack_strength_at_least") {
        override fun compare(cooledAttackStrength: Double, value: Double): Boolean {
            return cooledAttackStrength >= value
        }
    }

    object AtMost : FilterVictimCooledAttackStrength("victim_cooled_attack_strength_at_most") {
        override fun compare(cooledAttackStrength: Double, value: Double): Boolean {
            return cooledAttackStrength <= value
        }
    }

    object GreaterThan : FilterVictimCooledAttackStrength("victim_cooled_attack_strength_greater_than") {
        override fun compare(cooledAttackStrength: Double, value: Double): Boolean {
            return cooledAttackStrength > value
        }
    }

    object LowerThan : FilterVictimCooledAttackStrength("victim_cooled_attack_strength_lower_than") {
        override fun compare(cooledAttackStrength: Double, value: Double): Boolean {
            return cooledAttackStrength < value
        }
    }
}
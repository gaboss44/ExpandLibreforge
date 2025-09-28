package com.github.gaboss44.expandlibreforge.filters

import com.github.gaboss44.expandlibreforge.util.XpOrbFilter
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.entity.ExperienceOrb

sealed class FilterXpOrbExperience(id: String) : XpOrbFilter<NoCompileData, Int>(id) {
    override fun getValue(config: Config, data: TriggerData?, key: String): Int {
        return config.getIntFromExpression(key)
    }

    override fun isMet(data: TriggerData, value: Int, compileData: NoCompileData, orb: ExperienceOrb): Boolean {
        return compare(orb.experience, value)
    }

    abstract fun compare(xpCount: Int, value: Int): Boolean

    companion object {
        fun registerAllInto(category: Filters) {
            category.register(Equals)
            category.register(AtLeast)
            category.register(AtMost)
            category.register(GreaterThan)
            category.register(LowerThan)
        }
    }

    object Equals : FilterXpOrbCount("xp_orb_experience_equals") {
        override fun compare(xpCount: Int, value: Int): Boolean {
            return xpCount == value
        }
    }

    object AtLeast : FilterXpOrbCount("xp_orb_experience_at_least") {
        override fun compare(xpCount: Int, value: Int): Boolean {
            return xpCount >= value
        }
    }

    object AtMost : FilterXpOrbCount("xp_orb_experience_at_most") {
        override fun compare(xpCount: Int, value: Int): Boolean {
            return xpCount <= value
        }
    }

    object GreaterThan : FilterXpOrbCount("xp_orb_experience_greater_than") {
        override fun compare(xpCount: Int, value: Int): Boolean {
            return xpCount > value
        }
    }

    object LowerThan : FilterXpOrbCount("xp_orb_experience_lower_than") {
        override fun compare(xpCount: Int, value: Int): Boolean {
            return xpCount < value
        }
    }
}
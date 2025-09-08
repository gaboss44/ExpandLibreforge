package com.github.gaboss44.expandlibreforge.filters

object PlayerNoDamageTicksLowerThan : PlayerNoDamageTicksFilter("player_no_damage_ticks_lower_than") {
    override fun compare(playerValue: Int, filterValue: Int): Boolean {
        return playerValue < filterValue
    }
}

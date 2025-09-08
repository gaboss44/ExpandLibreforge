package com.github.gaboss44.expandlibreforge.filters

object PlayerNoDamageTicksGreaterThan : PlayerNoDamageTicksFilter("player_no_damage_ticks_greater_than") {
    override fun compare(playerValue: Int, filterValue: Int): Boolean {
        return playerValue > filterValue
    }
}


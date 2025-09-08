package com.github.gaboss44.expandlibreforge.filters

object PlayerNoDamageTicksEquals : PlayerNoDamageTicksFilter("player_no_damage_ticks_equals") {
    override fun compare(playerValue: Int, filterValue: Int): Boolean {
        return playerValue == filterValue
    }
}

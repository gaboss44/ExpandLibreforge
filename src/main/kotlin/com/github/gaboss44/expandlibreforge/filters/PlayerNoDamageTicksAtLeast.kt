package com.github.gaboss44.expandlibreforge.filters

object PlayerNoDamageTicksAtLeast : PlayerNoDamageTicksFilter("player_no_damage_ticks_at_least") {
    override fun compare(playerValue: Int, filterValue: Int): Boolean {
        return playerValue >= filterValue
    }
}

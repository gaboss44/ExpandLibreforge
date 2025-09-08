package com.github.gaboss44.expandlibreforge.filters

object PlayerNoDamageTicksAtMost : PlayerNoDamageTicksFilter("player_no_damage_ticks_at_most") {
    override fun compare(playerValue: Int, filterValue: Int): Boolean {
        return playerValue <= filterValue
    }
}

package com.github.gaboss44.expandlibreforge.features.combo

import org.bukkit.Bukkit
import java.util.UUID

data class Combo(
    val name: String,
    val playerId: UUID,
    val count: Int,
    val remainingTicks: Int,
    val phase: ComboPhase
) {
    val player get() = Bukkit.getPlayer(playerId)
}
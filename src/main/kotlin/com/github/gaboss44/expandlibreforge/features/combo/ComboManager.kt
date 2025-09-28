package com.github.gaboss44.expandlibreforge.features.combo

import com.github.gaboss44.expandlibreforge.events.combo.PlayerComboEndEvent
import com.github.gaboss44.expandlibreforge.events.combo.PlayerComboStartEvent
import com.github.gaboss44.expandlibreforge.events.combo.PlayerComboTickEvent
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.updateEffects
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Event
import java.util.UUID

object ComboManager {

    private val delegate = mutableMapOf<UUID, MutableMap<String, Combo>>()

    fun hasAnyCombo(playerId: UUID): Boolean {
        return delegate[playerId]?.isNotEmpty() ?: false
    }

    fun hasCombo(playerId: UUID, comboName: String): Boolean {
        return delegate[playerId]?.containsKey(comboName) ?: false
    }

    fun getCombo(playerId: UUID, comboName: String): Combo? {
        return delegate[playerId]?.get(comboName)
    }

    fun getCombos(playerId: UUID) = delegate[playerId]?.toMap()

    private fun tickCombos(event: Event?, player: Player, consumeTicks: Int = 1, renewalTicks: Int = 0): Boolean {
        val combos = delegate[player.uniqueId] ?: return false
        var updated = false
        for (combo in combos.values.toList()) {
            if (combo.remainingTicks > 0) {
                val tickEvent = PlayerComboTickEvent(
                    player = player,
                    combo = combo.copy(
                        phase = ComboPhase.TICK
                    ),
                    updateTicks = combo.remainingTicks - consumeTicks,
                    parent = event
                )
                Bukkit.getPluginManager().callEvent(tickEvent)
                if (!tickEvent.isCancelled) {
                    combos[combo.name] = combo.copy(
                        remainingTicks = tickEvent.updateTicks,
                        phase = ComboPhase.IDLE
                    )
                    updated = true
                } else combos[combo.name] = combo.copy(
                    phase = ComboPhase.IDLE
                )
            } else {
                val endEvent = PlayerComboEndEvent(
                    player = player,
                    combo = combo.copy(
                        phase = ComboPhase.END
                    ),
                    renewalTicks = renewalTicks,
                    parent = event
                )
                Bukkit.getPluginManager().callEvent(endEvent)
                if (!endEvent.isCancelled) {
                    combos.remove(combo.name)
                }
                else {
                    combos[combo.name] = combo.copy(
                        remainingTicks = endEvent.renewalTicks,
                        phase = ComboPhase.IDLE
                    )
                }
                updated = true
            }
        }
        if (combos.isEmpty()) {
            delegate.remove(player.uniqueId)
        }
        return updated
    }

    fun startCombo(playerId: UUID, comboName: String, count: Int, duration: Int) {
        val player = Bukkit.getPlayer(playerId) ?: return
        val combos = delegate.getOrPut(playerId) { mutableMapOf() }
        if (combos.containsKey(comboName)) {
            return
        }
        val combo = Combo(
            name = comboName,
            playerId = playerId,
            count = count,
            remainingTicks = duration,
            phase = ComboPhase.START
        )
        val event = PlayerComboStartEvent(
            player = player,
            combo = combo,
            startTicks = combo.remainingTicks
        )
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) {
            return
        }
        combos[comboName] = combo.copy(
            remainingTicks = event.startTicks,
            phase = ComboPhase.IDLE
        )
        player.toDispatcher().updateEffects()
    }

    fun endCombo(playerId: UUID, comboName: String) {
        val player = Bukkit.getPlayer(playerId) ?: return
        val combos = delegate[playerId] ?: return
        val combo = combos[comboName] ?: return
        if (combo.phase != ComboPhase.IDLE) return
        val event = PlayerComboEndEvent(
            player = player,
            combo = combo.copy(
                phase = ComboPhase.END,
                remainingTicks = 0
            ),
            renewalTicks = 0
        )
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) {
            combos[combo.name] = combo.copy(
                remainingTicks = event.renewalTicks,
                phase = ComboPhase.IDLE
            )
        } else {
            combos.remove(combo.name)
        }
        player.toDispatcher().updateEffects()
    }

    fun extendCombo(playerId: UUID, comboName: String, duration: Int, resetTicks: Boolean) {
        val player = Bukkit.getPlayer(playerId) ?: return
        val combos = delegate[playerId] ?: return
        val combo = combos[comboName] ?: return
        if (combo.phase != ComboPhase.IDLE) return
        val ticks = if (resetTicks) duration else combo.remainingTicks + duration
        val updatedCombo = combo.copy(
            count = combo.count + 1,
            remainingTicks = ticks
        )
        combos[comboName] = updatedCombo
        player.toDispatcher().updateEffects()
    }

    fun startOrExtendCombo(playerId: UUID, comboName: String, count: Int, duration: Int, resetTicks: Boolean) {
        if (hasCombo(playerId, comboName)) {
            extendCombo(playerId, comboName, duration, resetTicks)
        } else {
            startCombo(playerId, comboName, count, duration)
        }
    }

    fun tickAllCombos(event: Event?): List<UUID> {
        val updatedCombos = mutableListOf<UUID>()
        for (playerId in delegate.keys.toList()) {
            val player = Bukkit.getPlayer(playerId) ?: continue
            if (tickCombos(event, player)) updatedCombos.add(playerId)
        }
        return updatedCombos.toList()
    }
}

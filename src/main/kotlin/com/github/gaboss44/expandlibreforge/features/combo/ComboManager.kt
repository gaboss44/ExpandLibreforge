package com.github.gaboss44.expandlibreforge.features.combo

import com.github.gaboss44.expandlibreforge.events.combo.OfflineComboEndEvent
import com.github.gaboss44.expandlibreforge.events.combo.OfflineComboTickEvent
import com.github.gaboss44.expandlibreforge.events.combo.PlayerComboEndEvent
import com.github.gaboss44.expandlibreforge.events.combo.PlayerComboStartEvent
import com.github.gaboss44.expandlibreforge.events.combo.PlayerComboTickEvent
import com.willfp.libreforge.toDispatcher
import com.willfp.libreforge.updateEffects
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Event
import java.util.UUID
import kotlin.math.max

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

    private fun tickCombos(
        event: Event?,
        player: Player,
        consumeTicks: Int = 1,
        renewalTicks: Int = 0,
        updateEffects: Boolean = false
    ): Boolean {
        val combos = delegate[player.uniqueId] ?: return false
        var updated = false
        for (combo in combos.values.toList()) {
            if (combo.remainingTicks > 0) {
                val updateTicks = combo.remainingTicks - consumeTicks
                val tickEvent = PlayerComboTickEvent(
                    player = player,
                    combo = combo.copy(
                        phase = ComboPhase.TICK
                    ),
                    shouldUpdateEffects = updateEffects,
                    updateTicks = updateTicks,
                    parent = event
                )
                Bukkit.getPluginManager().callEvent(tickEvent)
                if (!tickEvent.isCancelled) {
                    combos[combo.name] = combo.copy(
                        remainingTicks = tickEvent.updateTicks,
                        maximumTicks = max(tickEvent.updateTicks, combo.maximumTicks),
                        phase = ComboPhase.IDLE
                    )
                    updated = tickEvent.shouldUpdateEffects
                } else combos[combo.name] = combo.copy(
                    phase = ComboPhase.IDLE
                )
            } else {
                val endEvent = PlayerComboEndEvent(
                    player = player,
                    combo = combo.copy(
                        phase = ComboPhase.END
                    ),
                    shouldUpdateEffects = updateEffects,
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
                        maximumTicks = max(endEvent.renewalTicks, combo.maximumTicks),
                        phase = ComboPhase.IDLE
                    )
                }
                updated = endEvent.shouldUpdateEffects
            }
        }
        if (combos.isEmpty()) {
            delegate.remove(player.uniqueId)
        }
        return updated
    }

    private fun tickOfflineCombos(
        event: Event?,
        playerId: UUID,
        consumeTicks: Int = 1,
        renewalTicks: Int = 0
    ) {
        val combos = delegate[playerId] ?: return
        for (combo in combos.values.toList()) {
            if (combo.remainingTicks > 0) {
                val updateTicks = combo.remainingTicks - consumeTicks
                val tickEvent = OfflineComboTickEvent(
                    combo = combo.copy(
                        phase = ComboPhase.TICK
                    ),
                    updateTicks = updateTicks,
                    parent = event
                )
                Bukkit.getPluginManager().callEvent(tickEvent)
                if (!tickEvent.isCancelled) {
                    combos[combo.name] = combo.copy(
                        remainingTicks = tickEvent.updateTicks,
                        maximumTicks = max(tickEvent.updateTicks, combo.maximumTicks),
                        phase = ComboPhase.IDLE
                    )
                } else combos[combo.name] = combo.copy(
                    phase = ComboPhase.IDLE
                )
            } else {
                val endEvent = OfflineComboEndEvent(
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
                        maximumTicks = max(endEvent.renewalTicks, combo.maximumTicks),
                        phase = ComboPhase.IDLE
                    )
                }
            }
        }
        if (combos.isEmpty()) {
            delegate.remove(playerId)
        }
    }

    fun startCombo(player: Player, comboName: String, count: Int, score: Double, duration: Int, updateEffects: Boolean) {
        val combos = delegate.getOrPut(player.uniqueId) { mutableMapOf() }
        if (combos.containsKey(comboName)) {
            return
        }
        val combo = Combo(
            name = comboName,
            playerId = player.uniqueId,
            count = count,
            remainingTicks = duration,
            initialTicks = duration,
            maximumTicks = duration,
            phase = ComboPhase.START,
            score = score
        )
        val event = PlayerComboStartEvent(
            player = player,
            combo = combo,
            shouldUpdateEffects = updateEffects,
            startTicks = combo.remainingTicks
        )
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) {
            return
        }
        combos[comboName] = combo.copy(
            remainingTicks = event.startTicks,
            initialTicks = event.startTicks,
            maximumTicks = event.startTicks,
            phase = ComboPhase.IDLE
        )
        if (event.shouldUpdateEffects) player.toDispatcher().updateEffects()
    }

    fun endCombo(player: Player, comboName: String, updateEffects: Boolean) {
        val combos = delegate[player.uniqueId] ?: return
        val combo = combos[comboName] ?: return
        if (combo.phase != ComboPhase.IDLE) return
        val event = PlayerComboEndEvent(
            player = player,
            combo = combo.copy(
                phase = ComboPhase.END,
                remainingTicks = 0
            ),
            shouldUpdateEffects = updateEffects,
            renewalTicks = 0
        )
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) {
            combos[combo.name] = combo.copy(
                remainingTicks = event.renewalTicks,
                maximumTicks = max(event.renewalTicks, combo.maximumTicks),
                phase = ComboPhase.IDLE
            )
        } else {
            combos.remove(combo.name)
        }
        if (event.shouldUpdateEffects) player.toDispatcher().updateEffects()
    }

    fun extendCombo(player: Player, comboName: String, score: Double, duration: Int, resetTicks: Boolean, updateEffects: Boolean) {
        val combos = delegate[player.uniqueId] ?: return
        val combo = combos[comboName] ?: return
        if (combo.phase != ComboPhase.IDLE) return
        val ticks = if (resetTicks) duration else combo.remainingTicks + duration
        val updatedCombo = combo.copy(
            count = combo.count + 1,
            remainingTicks = ticks,
            maximumTicks = max(combo.maximumTicks, ticks),
            score = combo.score + score,
        )
        combos[comboName] = updatedCombo
        if (updateEffects) player.toDispatcher().updateEffects()
    }

    fun startOrExtendCombo(player: Player, comboName: String, score: Double, count: Int, duration: Int, resetTicks: Boolean, updateEffects: Boolean) {
        if (hasCombo(player.uniqueId, comboName)) {
            extendCombo(player, comboName, score, duration, resetTicks, updateEffects)
        } else {
            startCombo(player, comboName, count, score, duration, updateEffects)
        }
    }

    fun tickAllCombos(event: Event?): List<UUID> {
        val updatedCombos = mutableListOf<UUID>()
        for (playerId in delegate.keys.toList()) {
            val player = Bukkit.getPlayer(playerId)
            if (player == null) {
                tickOfflineCombos(event, playerId)
            }
            else if (tickCombos(event, player)) {
                updatedCombos.add(playerId)
            }
        }
        return updatedCombos.toList()
    }
}

package com.github.gaboss44.expandlibreforge.conditions

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.Dispatcher
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.ProvidedHolder
import com.willfp.libreforge.conditions.Condition
import com.willfp.libreforge.get
import org.bukkit.entity.Player

object ConditionPlayerCurrentInput : Condition<NoCompileData>("player_current_input") {
    override fun isMet(
        dispatcher: Dispatcher<*>,
        config: Config,
        holder: ProvidedHolder,
        compileData: NoCompileData
    ): Boolean {
        val player = dispatcher.get<Player>() ?: return false
        val currentInput = player.currentInput

        if (config.has("is_sprint") && (config.getBool("is_sprint") != currentInput.isSprint)) return false

        if (config.has("is_left") && (config.getBool("is_left") != currentInput.isLeft)) return false

        if (config.has("is_jump") && (config.getBool("is_jump") != currentInput.isJump)) return false

        if (config.has("is_right") && (config.getBool("is_right") != currentInput.isRight)) return false

        if (config.has("is_sneak") && (config.getBool("is_sneak") != currentInput.isSneak)) return false

        if (config.has("is_backward") && (config.getBool("is_backward") != currentInput.isBackward)) return false

        if (config.has("is_forward") && (config.getBool("is_forward") != currentInput.isForward)) return false

        return true
    }
}
package com.github.gaboss44.expandlibreforge.filters

import com.github.gaboss44.expandlibreforge.events.input.PlayerInputUpdateEvent
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.filters.Filters
import com.willfp.libreforge.triggers.TriggerData

sealed class FilterPlayerInput(id: String) : Filter<NoCompileData, Boolean>(id) {
    final override fun getValue(config: Config, data: TriggerData?, key: String): Boolean {
        return config.getBool(key)
    }

    final override fun isMet(data: TriggerData, value: Boolean, compileData: NoCompileData): Boolean {
        val event = data.event as? PlayerInputUpdateEvent ?: return true
        return check(event, value)
    }
    
    open fun check(event: PlayerInputUpdateEvent, value: Boolean) : Boolean { return true }
    
    companion object {
        fun registerAllInto(category: Filters) {
            category.register(IsForward)
            category.register(IsBackward)
            category.register(IsLeft)
            category.register(IsRight)
            category.register(IsJump)
            category.register(IsSneak)
            category.register(IsSprint)
            category.register(ShouldUpdateEffects)
        }
    }

    object IsForward : FilterPlayerInput("player_input_is_forward") {
        override fun check(event: PlayerInputUpdateEvent, value: Boolean): Boolean {
            return event.parent.input.isForward == value
        }
    }

    object IsBackward : FilterPlayerInput("player_input_is_backward") {
        override fun check(event: PlayerInputUpdateEvent, value: Boolean): Boolean {
            return event.parent.input.isBackward == value
        }
    }

    object IsLeft : FilterPlayerInput("player_input_is_left") {
        override fun check(event: PlayerInputUpdateEvent, value: Boolean): Boolean {
            return event.parent.input.isLeft == value
        }
    }

    object IsRight : FilterPlayerInput("player_input_is_right") {
        override fun check(event: PlayerInputUpdateEvent, value: Boolean): Boolean {
            return event.parent.input.isRight == value
        }
    }

    object IsJump : FilterPlayerInput("player_input_is_jump") {
        override fun check(event: PlayerInputUpdateEvent, value: Boolean): Boolean {
            return event.parent.input.isJump == value
        }
    }

    object IsSneak : FilterPlayerInput("player_input_is_sneak") {
        override fun check(event: PlayerInputUpdateEvent, value: Boolean): Boolean {
            return event.parent.input.isSneak == value
        }
    }

    object IsSprint : FilterPlayerInput("player_input_is_sprint") {
        override fun check(event: PlayerInputUpdateEvent, value: Boolean): Boolean {
            return event.parent.input.isSprint == value
        }
    }
    
    object ShouldUpdateEffects : FilterPlayerInput("player_input_should_update_effects") {
        override fun check(event: PlayerInputUpdateEvent, value: Boolean): Boolean {
            return event.shouldUpdateEffects() == value
        }
    }
}
package com.github.gaboss44.expandlibreforge.events.combo

import com.github.gaboss44.expandlibreforge.events.ParentalEvent
import org.bukkit.event.Cancellable

interface ComboTickEvent : ComboEvent, Cancellable, ParentalEvent {
    var updateTicks: Int
}
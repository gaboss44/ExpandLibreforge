package com.github.gaboss44.expandlibreforge.events.combo

import com.github.gaboss44.expandlibreforge.events.ParentalEvent
import org.bukkit.event.Cancellable

interface ComboEndEvent : ComboEvent, Cancellable, ParentalEvent {
    var renewalTicks: Int
}
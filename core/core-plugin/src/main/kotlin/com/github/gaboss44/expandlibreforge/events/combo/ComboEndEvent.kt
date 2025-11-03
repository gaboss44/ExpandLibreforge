package com.github.gaboss44.expandlibreforge.events.combo

import com.github.gaboss44.expandlibreforge.events.Parental
import org.bukkit.event.Cancellable

interface ComboEndEvent : ComboEvent, Cancellable, Parental {
    var renewalTicks: Int
}
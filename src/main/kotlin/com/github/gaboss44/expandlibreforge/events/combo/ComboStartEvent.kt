package com.github.gaboss44.expandlibreforge.events.combo

import com.github.gaboss44.expandlibreforge.events.Parental
import org.bukkit.event.Cancellable

interface ComboStartEvent : ComboEvent, Cancellable, Parental {
    var startTicks: Int
}
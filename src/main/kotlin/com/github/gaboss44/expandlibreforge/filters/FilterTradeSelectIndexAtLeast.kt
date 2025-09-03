package com.github.gaboss44.expandlibreforge.filters

object FilterTradeSelectIndexAtLeast : TradeSelectIndexFilter("trade_select_index_at_least") {
    override fun compare(eventIndex: Int, filterIndex: Int): Boolean {
        return eventIndex >= filterIndex
    }
}

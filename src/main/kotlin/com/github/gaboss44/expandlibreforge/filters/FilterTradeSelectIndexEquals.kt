package com.github.gaboss44.expandlibreforge.filters

object FilterTradeSelectIndexEquals : TradeSelectIndexFilter("trade_select_index_equals") {
    override fun compare(eventIndex: Int, filterIndex: Int): Boolean {
        return eventIndex == filterIndex
    }
}

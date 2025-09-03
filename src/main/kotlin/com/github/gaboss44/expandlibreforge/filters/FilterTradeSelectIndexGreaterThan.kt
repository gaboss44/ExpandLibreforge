package com.github.gaboss44.expandlibreforge.filters

object FilterTradeSelectIndexGreaterThan : TradeSelectIndexFilter("trade_select_index_greater_than") {
    override fun compare(eventIndex: Int, filterIndex: Int): Boolean {
        return eventIndex > filterIndex
    }
}

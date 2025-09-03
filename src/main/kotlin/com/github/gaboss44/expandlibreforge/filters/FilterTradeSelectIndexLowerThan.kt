package com.github.gaboss44.expandlibreforge.filters

object FilterTradeSelectIndexLowerThan : TradeSelectIndexFilter("trade_select_index_lower_than") {
    override fun compare(eventIndex: Int, filterIndex: Int): Boolean {
        return eventIndex < filterIndex
    }
}


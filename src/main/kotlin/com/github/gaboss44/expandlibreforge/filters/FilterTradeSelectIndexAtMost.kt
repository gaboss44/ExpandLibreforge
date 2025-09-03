package com.github.gaboss44.expandlibreforge.filters

object FilterTradeSelectIndexAtMost : TradeSelectIndexFilter("trade_select_index_at_most") {
    override fun compare(eventIndex: Int, filterIndex: Int): Boolean {
        return eventIndex <= filterIndex
    }
}

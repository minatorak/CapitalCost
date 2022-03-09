package minatorak.capitalcost.api.summary

import java.math.BigDecimal

data class SummaryCapitalPrice(
    val name: String,
    var totalCoin: BigDecimal = BigDecimal.ZERO,
    val lastPrice: BigDecimal = BigDecimal.ZERO,
    var capitalPrice: BigDecimal = BigDecimal.ZERO,
    var quoteQty: BigDecimal = BigDecimal.ZERO,
    var totalTakeProfit: BigDecimal = BigDecimal.ZERO,
    var lostCommission: MutableMap<String, BigDecimal> = mutableMapOf(),
    val percent: BigDecimal = BigDecimal.ZERO
)
package minatorak.capitalcost.api.coin

import java.math.BigDecimal

data class TradeSummaryOfCoin(
    val name: String,
    var totalCoin: BigDecimal = BigDecimal.ZERO,
    var price: BigDecimal = BigDecimal.ZERO,
    var quoteQty: BigDecimal = BigDecimal.ZERO,
    var totalTakeProfit: BigDecimal = BigDecimal.ZERO,
    var lostCommission: MutableMap<String, BigDecimal> = mutableMapOf()
) {
    fun setValueAfterSellAll() {
        price = BigDecimal.ZERO
        quoteQty = BigDecimal.ZERO
        totalCoin = BigDecimal.ZERO
    }

    fun setValueEarningCoin() {
        price = BigDecimal.ZERO
        quoteQty = BigDecimal.ZERO
    }
}

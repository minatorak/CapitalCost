package minatorak.capitalcost.api.coin

import java.math.BigDecimal

data class TradeOfCoin(
    var totalCoin: BigDecimal = BigDecimal.ZERO,
    var price: BigDecimal = BigDecimal.ZERO,
    var quoteQty: BigDecimal = BigDecimal.ZERO,
) {
    fun setValueAfterSellAll() {
        price = BigDecimal.ZERO
        quoteQty = BigDecimal.ZERO
    }
}

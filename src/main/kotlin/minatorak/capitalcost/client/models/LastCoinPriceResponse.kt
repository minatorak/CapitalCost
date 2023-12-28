package minatorak.capitalcost.client.models

import java.math.BigDecimal

data class LastCoinPriceResponse(
    val symbol: String,
    val price: String
) {
    fun price() :BigDecimal {
        return BigDecimal.valueOf(price.toDouble())
    }
}

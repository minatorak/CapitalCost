package minatorak.capitalcost.api.coin

import java.math.BigDecimal

data class AveragePriceOfCoin(
    var totalCoin: BigDecimal = BigDecimal.ZERO,
    var price: BigDecimal = BigDecimal.ZERO,
    var quoteQty: BigDecimal = BigDecimal.ZERO
)

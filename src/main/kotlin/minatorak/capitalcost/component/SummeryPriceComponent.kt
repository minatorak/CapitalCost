package minatorak.capitalcost.component

import minatorak.capitalcost.api.coin.TradeSummaryOfCoin
import minatorak.capitalcost.client.models.TransactionTradeBySymbol
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode

@Component
class SummeryPriceComponent {
    private lateinit var sum: TradeSummaryOfCoin
    private lateinit var coinName: String
    private val log = LoggerFactory.getLogger(SummeryPriceComponent::class.java)

    fun calculatorTrade(coin: String, transactionList: List<TransactionTradeBySymbol>): TradeSummaryOfCoin {
        coinName = coin
        sum = TradeSummaryOfCoin(coinName)
        val lostCommission = mutableMapOf<String, BigDecimal>()
        transactionList.forEachIndexed { index, trx ->
            when {
                index == 0 -> initialOrder(trx)
                trx.isBuyer -> buyer(trx)
                !trx.isBuyer -> seller(trx)
            }
            if (coinName != trx.commissionAsset) {
                lostCommission.computeIfAbsent(trx.commissionAsset) { _ -> trx.commission() }
                lostCommission.computeIfPresent(trx.commissionAsset) { _, oldCommission -> oldCommission + trx.commission() }
            }
        }
        sum.lostCommission = lostCommission
        return sum
    }

    private fun initialOrder(trx: TransactionTradeBySymbol) {
        if (trx.commissionAsset == coinName)
            sum.totalCoin = trx.qty() - trx.commission()
        else
            sum.totalCoin = trx.qty()
        sum.price = trx.price()
        sum.quoteQty = trx.quoteQty()
    }

    private fun buyer(trx: TransactionTradeBySymbol) {
        sum.totalCoin = sum.totalCoin + trx.qty()
        if (trx.commissionAsset == coinName) sum.totalCoin - trx.commission()
        sum.quoteQty = sum.quoteQty + trx.quoteQty()
        sum.price = (sum.quoteQty / sum.totalCoin).setScale(4, RoundingMode.HALF_UP)
    }


    private fun seller(trx: TransactionTradeBySymbol) {
        sum.totalCoin = sum.totalCoin - trx.qty()
        val diffQuoteQty = sum.price * trx.qty()
        sum.quoteQty = sum.quoteQty - diffQuoteQty
        var commission = BigDecimal.ZERO
        if (trx.commissionAsset == coinName)
            commission = trx.commission()
        sum.totalTakeProfit =
            sum.totalTakeProfit + (trx.qty() * (trx.price() - sum.price)) - (commission * trx.price())

        if (sum.totalCoin.compareTo(BigDecimal.ZERO) <= 0)
            sum.setValueAfterSellAll()
        else
            sum.price = (sum.quoteQty / sum.totalCoin).setScale(4, RoundingMode.HALF_UP)
        if (sum.price.signum() < 0)
            log.info("trx: $trx")
    }
}
package minatorak.capitalcost.api.coin

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import minatorak.capitalcost.client.coin.CoinBinanceProvider
import minatorak.capitalcost.client.models.CoinInformation
import minatorak.capitalcost.client.models.OpenOrders
import minatorak.capitalcost.client.models.TransactionTradeBySymbol
import minatorak.capitalcost.client.trade.TradeBinanceProvider
import minatorak.capitalcost.properties.NameMarkets
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class CoinInformationService(
    private val coinBinanceProvider: CoinBinanceProvider,
    private val tradeBinanceProvider: TradeBinanceProvider,
    private val nameMarkets: NameMarkets
) {
    private val log = LoggerFactory.getLogger(CoinInformationService::class.java)
    private val objectMapper = jacksonObjectMapper()
    lateinit var coinName: String

    suspend fun getAllCoinList(): List<CoinInformation>? {
        val timestamp = System.currentTimeMillis()
        return coinBinanceProvider.getCoinInformation(timestamp)
    }

    suspend fun getAveragePrice(coin: String): MutableMap<String, TradeSummaryOfCoin> {
        coinName = coin.uppercase()
        val priceMap = mutableMapOf<String, TradeSummaryOfCoin>()
        for (market in nameMarkets.markets) {
            val symbol = coinName + market
            val commission = mutableMapOf<String, BigDecimal>()
            tradeBinanceProvider.accountTradeList(symbol)?.let { tradeList ->
                val job = CoroutineScope(Dispatchers.Unconfined).launch {
                    runCatching {
                        log.debug(objectMapper.writeValueAsString(tradeList))
                    }
                }
                val coinPrice = TradeSummaryOfCoin(coinName)
                tradeList.forEachIndexed { index, trx ->
                    try {
                        when {
                            index == 0 && trx.isBuyer -> addFirstTrx(coinPrice, trx)
                            index != 0 && trx.isBuyer -> calculatorBuyer(coinPrice, trx)
                            !trx.isBuyer -> calculatorSeller(coinPrice, trx)
                        }
                    } catch (ex: Exception) {
                        log.info("error calculator coinPrice: $coinPrice, trx: $trx, cause:${ex.message}")
                    }
                    if (coinName != trx.commissionAsset)
                        commission[trx.commissionAsset]?.apply {
                            this + trx.commission()
                        } ?: commission.put(trx.commissionAsset, trx.commission())
                }
                coinPrice.lostCommission = commission
                priceMap[symbol] = coinPrice
                job.join()
            }
        }
        return priceMap
    }

    private fun calculatorSeller(coin: TradeSummaryOfCoin, trx: TransactionTradeBySymbol) {
        coin.totalCoin = coin.totalCoin - trx.qty()
        val diffQuoteQty = coin.price * trx.qty()
        coin.quoteQty = coin.quoteQty - diffQuoteQty
        var commission = BigDecimal.ZERO
        if (trx.commissionAsset == coinName)
            commission = trx.commission()
        coin.totalTakeProfit =
            coin.totalTakeProfit + (trx.qty() * (trx.price() - coin.price)) - (commission * trx.price())
        if (coin.totalCoin.compareTo(BigDecimal.ZERO) == 0)
            coin.setValueAfterSellAll()
        else
            coin.price = (coin.quoteQty / coin.totalCoin).setScale(4, RoundingMode.HALF_UP)
        if (coin.price.compareTo(BigDecimal.ZERO) < 0)
            log.info("trx: $trx")
    }

    private fun calculatorBuyer(coin: TradeSummaryOfCoin, trx: TransactionTradeBySymbol) {
        coin.totalCoin = coin.totalCoin + trx.qty()
        if (trx.commissionAsset == coinName) coin.totalCoin - trx.commission()
        coin.quoteQty = coin.quoteQty + trx.quoteQty()
        coin.price = (coin.quoteQty / coin.totalCoin).setScale(4, RoundingMode.HALF_UP)
    }

    private fun addFirstTrx(coin: TradeSummaryOfCoin, trx: TransactionTradeBySymbol) {
        if (trx.commissionAsset == coinName)
            coin.totalCoin = trx.qty() - trx.commission()
        else
            coin.totalCoin = trx.qty()
        coin.price = trx.price()
        coin.quoteQty = trx.quoteQty()
    }

    suspend fun tradeBinanceHistory(): List<OpenOrders>? {
        return tradeBinanceProvider.getAllOrders()
    }

}
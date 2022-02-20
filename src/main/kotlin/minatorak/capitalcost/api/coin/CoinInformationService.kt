package minatorak.capitalcost.api.coin

import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.*
import minatorak.capitalcost.client.coin.CoinBinanceProvider
import minatorak.capitalcost.client.models.CoinInformation
import minatorak.capitalcost.client.models.OpenOrders
import minatorak.capitalcost.client.models.TransactionTradeBySymbol
import minatorak.capitalcost.client.trade.TradeBinanceProvider
import minatorak.capitalcost.properties.NameMarkets
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.RoundingMode

@Service
class CoinInformationService(
    private val coinBinanceProvider: CoinBinanceProvider,
    private val tradeBinanceProvider: TradeBinanceProvider,
    private val nameMarkets: NameMarkets
) {
    private val log = LoggerFactory.getLogger(CoinInformationService::class.java)
    private val objectMapper = jacksonObjectMapper()

    suspend fun getAllCoinList(): List<CoinInformation>? {
        val timestamp = System.currentTimeMillis()
        return coinBinanceProvider.getCoinInformation(timestamp)
    }

    suspend fun getAveragePrice(coin: String): MutableMap<String, AveragePriceOfCoin> {
        val priceMap = mutableMapOf<String, AveragePriceOfCoin>()
        for (market in nameMarkets.markets) {
            val symbol = coin.uppercase() + market
            tradeBinanceProvider.accountTradeList(symbol)?.let { tradeList ->
                CoroutineScope(Dispatchers.Unconfined).launch {
                    runCatching {
                        log.debug(objectMapper.writeValueAsString(tradeList))
                    }
                }
                val coinPrice = AveragePriceOfCoin()
                tradeList.forEachIndexed { index, trx ->
                    when {
                        index == 0 && trx.isBuyer -> addFirstTrx(coinPrice, trx)
                        index != 0 && trx.isBuyer -> calculatorBuyer(coinPrice, trx)
                        !trx.isBuyer -> calculatorSeller(coinPrice, trx)
                    }
                }
                priceMap[symbol] = coinPrice
            }
        }
        return priceMap
    }

    private fun calculatorSeller(coin: AveragePriceOfCoin, trx: TransactionTradeBySymbol) {
        coin.totalCoin = coin.totalCoin - trx.qty()
        val diffQuoteQty = coin.price * trx.qty()
        coin.quoteQty = coin.quoteQty - diffQuoteQty
        coin.price = (coin.quoteQty / coin.totalCoin).setScale(4, RoundingMode.HALF_UP)
    }

    private fun calculatorBuyer(coin: AveragePriceOfCoin, trx: TransactionTradeBySymbol) {
        coin.totalCoin = coin.totalCoin + trx.qty()
        coin.quoteQty = coin.quoteQty + trx.quoteQty()
        coin.price = (coin.quoteQty / coin.totalCoin).setScale(4, RoundingMode.HALF_UP)
    }

    private fun addFirstTrx(coin: AveragePriceOfCoin, trx: TransactionTradeBySymbol) {
        coin.totalCoin = trx.qty()
        coin.price = trx.price()
        coin.quoteQty = trx.quoteQty()
    }

    suspend fun tradeBinanceHistory(): List<OpenOrders>? {
        return tradeBinanceProvider.getAllOrders()
    }

}
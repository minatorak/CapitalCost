package minatorak.capitalcost.api.coin

import minatorak.capitalcost.client.coin.CoinBinanceProvider
import minatorak.capitalcost.client.models.CoinInformation
import minatorak.capitalcost.client.models.OpenOrders
import minatorak.capitalcost.client.trade.TradeBinanceProvider
import minatorak.capitalcost.properties.NameMarkets
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CoinInformationService(
    private val coinBinanceProvider: CoinBinanceProvider,
    private val tradeBinanceProvider: TradeBinanceProvider,
    private val nameMarkets: NameMarkets
) {
    private val log = LoggerFactory.getLogger(CoinInformationService::class.java)

    suspend fun getAllCoinList(): List<CoinInformation>? {
        val timestamp = System.currentTimeMillis()
        return coinBinanceProvider.getCoinInformation(timestamp)
    }

    suspend fun getAveragePrice(coin: String) {
        for (market in nameMarkets.markets) {
            tradeBinanceProvider.accountTradeList(coin + market)?.let {
                log.info("TradeList $it")
            }
        }
    }

    suspend fun tradeBinanceHistory(): List<OpenOrders>? {
        return tradeBinanceProvider.getAllOrders()
    }

}
package minatorak.capitalcost.api.summary

import kotlinx.coroutines.delay
import minatorak.capitalcost.api.coin.TradeSummaryOfCoin
import minatorak.capitalcost.client.coin.CoinBinanceProvider
import minatorak.capitalcost.client.trade.TradeBinanceProvider
import minatorak.capitalcost.component.SummeryPriceComponent
import minatorak.capitalcost.properties.NameMarkets
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SummaryService(
    private val coinBinanceProvider: CoinBinanceProvider,
    private val tradeBinanceProvider: TradeBinanceProvider,
    private val nameMarkets: NameMarkets,
    private val summaryPriceComponent: SummeryPriceComponent
) {
    private val log = LoggerFactory.getLogger(SummaryService::class.java)
    private val listSummary by lazy { mutableListOf<TradeSummaryOfCoin>() }

    suspend fun getSummary(): MutableList<TradeSummaryOfCoin> {
        return listSummary
    }

    suspend fun sum() {
        coinBinanceProvider.getCoinInformation(System.currentTimeMillis())?.let { listInfo ->
            for (coinInfo in listInfo) {
                if (coinInfo.free == "0")
                    continue

                nameMarkets.markets.forEach { nameOfMarket ->
                    val symbol = coinInfo.coin + nameOfMarket
                    if (nameMarkets.mapMarketPair.contains(symbol)) {
                        delay(500) // because binance error too many request
                        tradeBinanceProvider.accountTradeList(symbol)?.let { tradList ->
                            if (tradList.isNotEmpty()) {
                                listSummary.add(summaryPriceComponent.calculatorTrade(coinInfo.coin, tradList))
                            }
                        }
                    }
                }
                log.info("${coinInfo.coin} done.")
            }
            log.info("complete summary")
        }
    }


}

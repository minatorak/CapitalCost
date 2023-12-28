package minatorak.capitalcost.api.summary

import kotlinx.coroutines.delay
import minatorak.capitalcost.api.coin.TradeSummaryOfCoin
import minatorak.capitalcost.client.BinancePublicClient
import minatorak.capitalcost.client.coin.CoinBinanceProvider
import minatorak.capitalcost.client.trade.TradeBinanceProvider
import minatorak.capitalcost.component.SummeryPriceComponent
import minatorak.capitalcost.properties.NameMarkets
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class SummaryService(
    private val coinBinanceProvider: CoinBinanceProvider,
    private val binancePublicClient: BinancePublicClient,
    private val tradeBinanceProvider: TradeBinanceProvider,
    private val nameMarkets: NameMarkets,
    private val summaryPriceComponent: SummeryPriceComponent
) {
    private val log = LoggerFactory.getLogger(SummaryService::class.java)
    private val listSummary by lazy { mutableMapOf<String, TradeSummaryOfCoin>() }
    private val ONE_HUNDRED = BigDecimal.valueOf(100)

    suspend fun getSummary(): MutableList<SummaryCapitalPrice> {
        val lastPriceResponse = binancePublicClient.latestPriceAllSymbols()
        val response = mutableListOf<SummaryCapitalPrice>()
        lastPriceResponse.forEach { lastPrice ->
            listSummary[lastPrice.symbol]?.let {
                response.add(
                    SummaryCapitalPrice(
                        name = lastPrice.symbol,
                        lastPrice = lastPrice.price(),
                        totalCoin = it.totalCoin,
                        capitalPrice = it.price,
                        quoteQty = it.quoteQty,
                        totalTakeProfit = it.totalTakeProfit,
                        lostCommission = it.lostCommission,
                        percent = if (it.price.signum() > 0) ((lastPrice.price() - it.price) / it.price) * ONE_HUNDRED else BigDecimal.ZERO
                    )
                )
            }
        }
        return response.apply {
            this.sortByDescending { it.quoteQty }
        }
    }

    suspend fun sum() {
        val coinList = mutableSetOf<String>()
        coinBinanceProvider.getCoinInformation(System.currentTimeMillis())?.let { listInfo ->
            listInfo.map { it.coin }.apply {
                coinList.addAll(this)
            }
        }
        coinList.let { coinNameList ->
            for (coinName in coinNameList) {
                nameMarkets.markets.forEach { nameOfMarket ->
                    val symbol = coinName + nameOfMarket
                    if (nameMarkets.mapMarketPair.contains(symbol)) {
                        delay(500) // because binance error too many request
                        tradeBinanceProvider.accountTradeList(symbol)?.let { tradList ->
                            if (tradList.isNotEmpty()) {
                                listSummary[symbol] = summaryPriceComponent.calculatorTrade(symbol, tradList)
                            }
                        }
//                    }
                    }
                }
                log.info("${coinName} done.")
            }
            log.info("complete summary")
        }
    }


}

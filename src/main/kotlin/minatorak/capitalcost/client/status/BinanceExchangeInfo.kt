package minatorak.capitalcost.client.status

import kotlinx.coroutines.runBlocking
import minatorak.capitalcost.api.summary.SummaryService
import minatorak.capitalcost.client.BinanceClient
import minatorak.capitalcost.properties.BinanceEndpoint
import minatorak.capitalcost.properties.NameMarkets
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class BinanceExchangeInfo(
    private val binanceClient: BinanceClient,
    private val summaryService: SummaryService,
    private val binanceEndpoint: BinanceEndpoint,
    private val nameMarkets: NameMarkets
) {

    private val log = LoggerFactory.getLogger(BinanceExchangeInfo::class.java)

    @EventListener(ApplicationStartedEvent::class)
    fun onApplicationEvent() {
        runBlocking {
            val symbols =
                binanceClient.exchangeInfoList(UriComponentsBuilder.fromUriString(binanceEndpoint.baseEndpoint))?.symbols
            symbols?.let {
                if (nameMarkets.markets.isEmpty()) nameMarkets.markets.addAll(it.map { it.quoteAsset })
                else log.info("force market : ${nameMarkets.markets}")
                nameMarkets.mapMarketPair.addAll(it.map { it.symbol })
            }
            log.info("add markets pair : ${nameMarkets.mapMarketPair}")
            summaryService.sum()
        }
    }
}
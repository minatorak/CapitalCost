package minatorak.capitalcost.client

import minatorak.capitalcost.client.models.LastCoinPriceResponse
import minatorak.capitalcost.properties.BinanceEndpoint
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.util.UriComponentsBuilder

@Component
class BinancePublicClient(
    private val webClient: WebClient,
    private val binanceEndpoint: BinanceEndpoint
) {

    suspend fun latestPriceAllSymbols() =
        webClient.get()
            .uri(
                UriComponentsBuilder
                    .fromUriString(binanceEndpoint.baseEndpoint)
                    .path(BinancePath.symbolPriceTicker)
                    .toUriString()
            )
            .retrieve()
            .awaitBody<List<LastCoinPriceResponse>>()

}
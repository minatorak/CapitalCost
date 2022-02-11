package minatorak.capitalcost.client

import minatorak.capitalcost.client.models.CoinInformation
import minatorak.capitalcost.client.models.OpenOrders
import minatorak.capitalcost.client.models.TradesBySymbol
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import org.springframework.web.util.UriComponentsBuilder

@Component
class BinanceClient(private val webClient: WebClient) {

    companion object {
        private const val API_KEY = "X-MBX-APIKEY"
    }


    suspend fun callAllCoinInformationUser(uri : UriComponentsBuilder, apiKey: String): List<CoinInformation>? {
        return webClient.get()
            .uri(uri
                .path(BinancePath.allCoinInformationUser)
                .toUriString()
            )
            .header(API_KEY, apiKey)
            .retrieve()
            .awaitBodyOrNull()
    }

    suspend fun getAllOrderSymbol(uri : UriComponentsBuilder, apiKey: String): List<OpenOrders>? {
        return webClient.get()
            .uri(
                uri.path(BinancePath.currentOpenOrders)
                    .toUriString()
            )
            .header(API_KEY, apiKey)
            .retrieve()
            .awaitBodyOrNull()
    }

    suspend fun accountTradeList(uri : UriComponentsBuilder, apiKey: String): List<TradesBySymbol>? {
        return webClient.get()
            .uri(
                uri.path(BinancePath.accountTradeList)
                    .toUriString()
            )
            .header(API_KEY, apiKey)
            .retrieve()
            .awaitBodyOrNull()
    }
}

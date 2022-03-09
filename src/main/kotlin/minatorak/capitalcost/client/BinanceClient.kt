package minatorak.capitalcost.client

import BinanceExchangeInfoStatus
import minatorak.capitalcost.client.models.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.*
import org.springframework.web.util.UriComponentsBuilder

@Component
class BinanceClient(private val webClient: WebClient) {

    companion object {
        private const val API_KEY = "X-MBX-APIKEY"
        val log = LoggerFactory.getLogger(BinanceClient::class.java)
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

    suspend fun callGetAccountEarning(uri: UriComponentsBuilder, apiKey: String): MiningAccountEarningResponse? {
        return webClient.get()
            .uri(
                uri.path(BinancePath.miningAccountEarning)
                    .toUriString()
            )
            .header(API_KEY, apiKey)
            .httpRequest {

            }
            .awaitExchange {
                return@awaitExchange it.awaitBody()
            }
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

    suspend fun accountTradeList(uri : UriComponentsBuilder, apiKey: String): List<TransactionTradeBySymbol>? {
        return webClient.get()
            .uri(
                uri.path(BinancePath.accountTradeList)
                    .toUriString()
            )
            .header(API_KEY, apiKey)
            .retrieve()
            .awaitBodyOrNull()
    }

    suspend fun exchangeInfoList(uri : UriComponentsBuilder): BinanceExchangeInfoStatus? {
        return webClient.get()
            .uri(
                uri.path(BinancePath.exchangeInfo)
                    .toUriString()
            )
            .retrieve()
            .awaitBodyOrNull()
    }
}

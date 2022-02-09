package minatorak.capitalcost.client

import minatorak.capitalcost.client.models.CoinInformation
import minatorak.capitalcost.properties.BinanceEndpoint
import minatorak.capitalcost.properties.UserProperties
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import org.springframework.web.util.UriBuilder
import org.springframework.web.util.UriComponentsBuilder

@Component
class BinanceClient(
    private val webClient: WebClient,
//    private val binanceEndpoint: BinanceEndpoint,
//    private val userProperties: UserProperties
) {

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
//    suspend fun allCoinInformationUser(hmacSHA256: String, timestamp: Long): List<CoinInformation>? {
//        return webClient.get()
//            .uri(
//                UriComponentsBuilder.fromUriString(binanceEndpoint.baseEndpoint)
//                    .path(BinancePath.allCoinInformationUser)
//                    .queryParam("timestamp", timestamp)
//                    .queryParam("signature", hmacSHA256)
//                    .toUriString()
//            )
//            .header(
//                API_KEY, userProperties.apiKey
//            )
//            .retrieve()
//            .awaitBodyOrNull()
//    }
}

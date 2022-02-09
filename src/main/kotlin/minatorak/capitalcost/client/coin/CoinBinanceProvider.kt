package minatorak.capitalcost.client.coin

import minatorak.capitalcost.SignatureComponent
import minatorak.capitalcost.client.BinanceClient
import minatorak.capitalcost.client.models.CoinInformation
import minatorak.capitalcost.properties.BinanceEndpoint
import minatorak.capitalcost.properties.UserProperties
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class CoinBinanceProvider(
    private val binanceClient: BinanceClient,
    private val signatureComponent: SignatureComponent,
    private val userProperties: UserProperties,
    private val binanceEndpoint: BinanceEndpoint
) {

    suspend fun getCoinInformation(timestamp: Long): List<CoinInformation>? {
        val data = "timestamp=$timestamp"
        val hmacSHA256 = signatureComponent.hmacWithBouncyCastle(data, userProperties.secretKey)
        val uri = UriComponentsBuilder
            .fromUriString(binanceEndpoint.baseEndpoint)
            .queryParam("timestamp", timestamp)
            .queryParam("signature", hmacSHA256)
        return binanceClient.callAllCoinInformationUser(uri, userProperties.apiKey)
    }

}

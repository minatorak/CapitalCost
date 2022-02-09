package minatorak.capitalcost.api.coin

import minatorak.capitalcost.SignatureComponent
import minatorak.capitalcost.client.BinanceClient
import minatorak.capitalcost.client.models.AllCoinInformationUserResponse
import minatorak.capitalcost.client.models.CoinInformation
import minatorak.capitalcost.properties.UserProperties
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.json

@Component
class CoinHandler(
    private val binanceClient: BinanceClient,
    private val signatureComponent: SignatureComponent,
    private val userProperties: UserProperties
) {
    private val log = LoggerFactory.getLogger(CoinHandler::class.java)

    suspend fun list(request: ServerRequest): ServerResponse {
        val timestamp = System.currentTimeMillis()
        val request = "timestamp=$timestamp"
        var response: List<CoinInformation>? = null
        signatureComponent.hmacWithBouncyCastle(request, userProperties.secretKey)?.let {
            response = binanceClient.allCoinInformationUser(it, timestamp)
        }
        log.info("response $response")
        return ServerResponse.ok().json().bodyValueAndAwait("Hello")
    }
}

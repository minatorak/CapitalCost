package minatorak.capitalcost.api.coin

import minatorak.capitalcost.SignatureComponent
import minatorak.capitalcost.client.BinanceClient
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
    private val coinInformationService: CoinInformationService
    ) {
    private val log = LoggerFactory.getLogger(CoinHandler::class.java)

    suspend fun list(request: ServerRequest): ServerResponse {
        val response = coinInformationService.getAllCoinList()?.let { coinList ->

            for (coin in coinList) {
                log.info("coin: ${coin.coin} name: ${coin.name} free: ${coin.free} freeze: ${coin.freeze} locked: ${coin.locked}")
            }

        }
        log.info("response $response")
        return ServerResponse.ok().json().bodyValueAndAwait("Hello")
    }
}

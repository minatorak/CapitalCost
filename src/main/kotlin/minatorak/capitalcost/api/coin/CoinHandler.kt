package minatorak.capitalcost.api.coin

import minatorak.capitalcost.client.trade.TradeBinanceProvider
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.json
import java.math.BigDecimal

@Component
class CoinHandler(
    private val coinInformationService: CoinInformationService,
    private val tradeBinanceProvider: TradeBinanceProvider
) {
    private val log = LoggerFactory.getLogger(CoinHandler::class.java)

    suspend fun list(request: ServerRequest): ServerResponse {
        val myCoinList = mutableMapOf<String, BigDecimal>()
        coinInformationService.getAllCoinList()?.let { coinList ->
            for (coin in coinList) {
                if (coin.free != "0") {
                    log.info("coin: ${coin.coin} name: ${coin.name} free: ${coin.free} freeze: ${coin.freeze} locked: ${coin.locked}")
                    myCoinList[coin.coin] = BigDecimal(coin.free)
                }
            }
        }
        tradeBinanceProvider.getAllOrders()?.let {
            log.info("get trade history $it")
        }
        return ServerResponse.ok().json().bodyValueAndAwait(myCoinList)
    }

    suspend fun average(request: ServerRequest): ServerResponse {
        val coin = request.queryParam("coin").get()
        val response = coinInformationService.getAveragePrice(coin)
        return ServerResponse.ok().bodyValueAndAwait(response)
    }
}

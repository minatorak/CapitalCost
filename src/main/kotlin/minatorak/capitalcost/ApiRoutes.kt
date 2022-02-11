package minatorak.capitalcost

import minatorak.capitalcost.api.coin.CoinHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class ApiRoutes {

    @Bean
    fun apiRouters(coinHandler : CoinHandler) = coRouter {
        "coin/list".nest {
            GET("/spot", coinHandler::list)
            GET("/average", coinHandler::average)
        }
    }
}
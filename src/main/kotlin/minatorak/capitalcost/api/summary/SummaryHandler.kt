package minatorak.capitalcost.api.summary

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import java.math.BigDecimal

@Configuration
class SummaryHandler(private val summaryService: SummaryService) {
    private val log = LoggerFactory.getLogger(SummaryHandler::class.java)

    suspend fun sum(request: ServerRequest): ServerResponse {
        val sumList = summaryService.getSummary()
        var allProfit = BigDecimal.ZERO
        sumList.forEach {
            allProfit += it.totalTakeProfit
        }
        log.info("all profit $allProfit")
        return ServerResponse.ok().bodyValueAndAwait(sumList)
    }
}
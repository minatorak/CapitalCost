package minatorak.capitalcost.api.summary

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Configuration
class SummaryHandler(private val summaryService: SummaryService) {

    suspend fun sum(request: ServerRequest) : ServerResponse {
        return ServerResponse.ok().bodyValueAndAwait(summaryService.getSummary())
    }
}
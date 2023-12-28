package minatorak.capitalcost.configs

import io.netty.channel.ChannelOption
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@Configuration
class WebClientConfiguration {
    companion object {
        private const val ONE_SECOND = 1000
        private const val size = 16 * 1024 * 1024
    }

    @Bean
    fun webClient() = WebClient
        .builder()
        .clientConnector(
            ReactorClientHttpConnector(
                HttpClient
                    .create()
                    .secure { it.sslContext(insecureTrust) }
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30 * ONE_SECOND)
                    .doOnConnected { connection ->
                        val connectionTimeout = 30
                        connection.addHandlerLast(ReadTimeoutHandler(connectionTimeout))
                        connection.addHandlerLast(WriteTimeoutHandler(connectionTimeout))
                    }
            )
        ).exchangeStrategies(
            ExchangeStrategies.builder()
                .codecs{codecs -> codecs.defaultCodecs().maxInMemorySize(size)}
                .build()
        ).build()

    private val insecureTrust =
        SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
}
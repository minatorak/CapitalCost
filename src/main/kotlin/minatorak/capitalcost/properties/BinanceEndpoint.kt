package minatorak.capitalcost.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("binance")
data class BinanceEndpoint (val baseEndpoint: String)
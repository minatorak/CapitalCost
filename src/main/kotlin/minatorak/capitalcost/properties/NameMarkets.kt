package minatorak.capitalcost.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Configuration

@ConfigurationProperties()
@ConstructorBinding
class NameMarkets(val markets: List<String>)
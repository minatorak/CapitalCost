package minatorak.capitalcost.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties()
@ConstructorBinding
class NameMarkets(val markets: MutableSet<String>, val mapMarketPair: MutableList<String>)
package minatorak.capitalcost.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("user")
data class UserProperties(val apiKey: String, val secretKey: String)
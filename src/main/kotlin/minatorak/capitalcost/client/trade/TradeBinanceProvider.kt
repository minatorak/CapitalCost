package minatorak.capitalcost.client.trade

import minatorak.capitalcost.SignatureComponent
import minatorak.capitalcost.client.BinanceClient
import minatorak.capitalcost.client.models.OpenOrders
import minatorak.capitalcost.client.models.TransactionTradeBySymbol
import minatorak.capitalcost.properties.BinanceEndpoint
import minatorak.capitalcost.properties.UserProperties
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class TradeBinanceProvider(
    private val binanceClient: BinanceClient,
    private val signatureComponent: SignatureComponent,
    private val userProperties: UserProperties,
    private val binanceEndpoint: BinanceEndpoint
) {

    suspend fun getAllOrders(): List<OpenOrders>? {
        val timeStamp = System.currentTimeMillis().toString()
        val data = "recvWindow=10000&timestamp=$timeStamp"
        val hmacSHA256 = signatureComponent.hmacWithBouncyCastle(data, userProperties.secretKey)
        val uri = UriComponentsBuilder.fromUriString(binanceEndpoint.baseEndpoint)
            .queryParam("recvWindow", 10000)
            .queryParam("timestamp", timeStamp)
            .queryParam("signature", hmacSHA256)
        return binanceClient.getAllOrderSymbol(uri, userProperties.apiKey)
    }

    suspend fun accountTradeList(symbol: String): List<TransactionTradeBySymbol>? {
        val timeStamp = System.currentTimeMillis().toString()
        val data = "symbol=$symbol&limit=1000&timestamp=$timeStamp"
        val hmacSHA256 = signatureComponent.hmacWithBouncyCastle(data, userProperties.secretKey)
        val uri = UriComponentsBuilder.fromUriString(binanceEndpoint.baseEndpoint)
            .queryParam("symbol", symbol)
            .queryParam("limit", 1000)
            .queryParam("timestamp", timeStamp)
            .queryParam("signature", hmacSHA256)
        try {
            return binanceClient.accountTradeList(uri, userProperties.apiKey)
        } catch (ex: Exception) {
            return null
        }
    }
}
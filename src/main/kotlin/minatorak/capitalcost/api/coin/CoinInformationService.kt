package minatorak.capitalcost.api.coin

import minatorak.capitalcost.client.coin.CoinBinanceProvider
import minatorak.capitalcost.client.models.CoinInformation
import org.springframework.stereotype.Service

@Service
class CoinInformationService(private val coinBinanceProvider: CoinBinanceProvider) {
    suspend fun getAllCoinList(): List<CoinInformation>? {
        val timestamp = System.currentTimeMillis()
        return coinBinanceProvider.getCoinInformation(timestamp)
    }

}
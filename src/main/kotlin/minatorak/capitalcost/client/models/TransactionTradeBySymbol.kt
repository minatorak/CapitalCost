package minatorak.capitalcost.client.models

import java.math.BigDecimal

data class TransactionTradeBySymbol(
    val symbol: String,
    val id: Long,
    val orderId: Long,
    val orderListId: Long,
    val price: String, // ราคา
    val qty: String, // จำนวณเหรียญที่แลกเปลี่ยน
    val quoteQty: String, // ราคาตลาด
    val commission: String, //
    val commissionAsset: String,
    val time: Long,
    val isBuyer: Boolean,
    val isMaker: Boolean,
    val isBeatMatch: Boolean
) {
    fun qty(): BigDecimal {
        return BigDecimal.valueOf(qty.toDouble())
    }

    fun quoteQty(): BigDecimal {
        return BigDecimal.valueOf(quoteQty.toDouble())
    }

    fun price(): BigDecimal {
        return BigDecimal.valueOf(price.toDouble())
    }

    fun commission(): BigDecimal {
        return BigDecimal.valueOf(commission.toDouble())
    }
}


//[
//{
//    "symbol": "BNBBTC",
//    "id": 28457,
//    "orderId": 100234,
//    "orderListId": -1, //Unless OCO, the value will always be -1
//    "price": "4.00000100",
//    "qty": "12.00000000",
//    "quoteQty": "48.000012",
//    "commission": "10.10000000",
//    "commissionAsset": "BNB",
//    "time": 1499865549590,
//    "isBuyer": true,
//    "isMaker": false,
//    "isBestMatch": true
//}
//]
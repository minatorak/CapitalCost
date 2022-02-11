package minatorak.capitalcost.client.models

data class TradesBySymbol(
    val symbol: String,
    val id: Int,
    val orderId: Int,
    val orderListId: Int,
    val price: String, // ราคา
    val qty: String, // จำนวณเหรียญที่แลกเปลี่ยน
    val quoteQty: String, // ราคาตลาด
    val commission: String, //
    val commissionAsser: String?,
    val time: Long,
    val isBuyer: Boolean,
    val isMaker: Boolean,
    val isBeatMatch: Boolean
)


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
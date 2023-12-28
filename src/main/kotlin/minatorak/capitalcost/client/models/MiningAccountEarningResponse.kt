package minatorak.capitalcost.client.models

import java.math.BigDecimal

data class MiningAccountEarningResponse(
    val code: Int,
    val msg: String,
    val data: MiningAccount
)

data class MiningAccount(
    val accountProfits: List<CoinProfit>,
    val totalNum: Int,
    val pageSize: Int
)

class CoinProfit(
    val time: Long,
    val coinName: String,
    val amount: BigDecimal
)
//{
//    "code": 0,
//    "msg": "",
//    "data":
//    {
//        "accountProfits": [
//        {
//            "time": 1607443200000,
//            "coinName": "BTC",   // Coin
//            "type": 2,           // 0:Referral 1：Refund 2：Rebate
//            "puid": 59985472,    //Sub-account id
//            "subName": "vdvaghani", //Mining account
//            "amount": 0.09186957    //Amount
//        }
//        ],
//        "totalNum": 3,          // Total records
//        "pageSize": 20          // Size of one page
//    }
//}



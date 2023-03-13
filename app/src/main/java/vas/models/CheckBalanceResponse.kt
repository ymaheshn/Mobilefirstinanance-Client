package vas.models

import com.google.gson.annotations.SerializedName

data class CheckBalanceResponse(

    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : CheckBalanceData?   = CheckBalanceData()

)

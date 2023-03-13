package vas.models

import com.google.gson.annotations.SerializedName

data class CheckBalanceData(


    @SerializedName("portfolio" ) var portfolio : ArrayList<PortfolioCheckBalance> = arrayListOf()
)

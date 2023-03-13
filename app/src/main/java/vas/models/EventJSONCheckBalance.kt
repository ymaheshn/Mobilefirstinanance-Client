package vas.models

import com.google.gson.annotations.SerializedName

data class EventJSONCheckBalance(
    @SerializedName("Transaction") var Transaction: TransactionCheckBalance? = TransactionCheckBalance()
)

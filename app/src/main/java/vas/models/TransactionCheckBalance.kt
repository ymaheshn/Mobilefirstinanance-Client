package vas.models

import com.google.gson.annotations.SerializedName

data class TransactionCheckBalance(

    @SerializedName("Value"                  ) var Value                  : Int?    = null,
    @SerializedName("Units"                  ) var Units                  : String? = null,
    @SerializedName("DebitPaymentChannelID"  ) var DebitPaymentChannelID  : String? = null,
    @SerializedName("CreditPaymentChannelID" ) var CreditPaymentChannelID : String? = null,
    @SerializedName("PaymentMethod"          ) var PaymentMethod          : String? = null,
    @SerializedName("LedgerDetails"          ) var LedgerDetails          : String? = null,
    @SerializedName("RemainingAmount"        ) var RemainingAmount        : Int?    = null
)

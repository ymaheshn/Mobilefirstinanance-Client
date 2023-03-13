package vas.models

import com.google.gson.annotations.SerializedName

data class ContractJSONCheckBalance(

    @SerializedName("Terms" ) var Terms : ArrayList<TermsCheckBalance> = arrayListOf()
)

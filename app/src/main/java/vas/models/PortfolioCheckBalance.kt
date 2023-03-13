package vas.models

import com.google.gson.annotations.SerializedName

data class PortfolioCheckBalance(
    @SerializedName("summeryID"         ) var summeryID         : Int?          = null,
    @SerializedName("syncDate"          ) var syncDate          : String?       = null,
    @SerializedName("profileID"         ) var profileID         : String?       = null,
    @SerializedName("profileIdentifier" ) var profileIdentifier : String?       = null,
    @SerializedName("productName"       ) var productName       : String?       = null,
    @SerializedName("profileJSON"       ) var profileJSON       : ProfileJsonCheckBalance?  = ProfileJsonCheckBalance(),
    @SerializedName("contractUUID"      ) var contractUUID      : String?       = null,
    @SerializedName("contractJSON"      ) var contractJSON      : ContractJSONCheckBalance? = ContractJSONCheckBalance(),
    @SerializedName("contractID"        ) var contractID        : String?       = null,
    @SerializedName("units"             ) var units             : String?       = null,
    @SerializedName("eventTime"         ) var eventTime         : String?       = null,
    @SerializedName("eventValue"        ) var eventValue        : Int?          = null,
    @SerializedName("eventType"         ) var eventType         : String?       = null,
    @SerializedName("eventID"           ) var eventID           : String?       = null,
    @SerializedName("rootUserID"        ) var rootUserID        : String?       = null,
    @SerializedName("eventJSON"         ) var eventJSON         : EventJSONCheckBalance?    = EventJSONCheckBalance(),
    @SerializedName("rePayment"         ) var rePayment         : String?       = null,
    @SerializedName("status"            ) var status            : String?       = null,
    @SerializedName("processTime"       ) var processTime       : String?       = null

)

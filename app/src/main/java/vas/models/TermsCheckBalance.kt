package vas.models

import com.google.gson.annotations.SerializedName

class TermsCheckBalance {
    @SerializedName("DayCountConvention"               ) var DayCountConvention               : String? = null
    @SerializedName("CycleOfInterestPayment"           ) var CycleOfInterestPayment           : String? = null
    @SerializedName("InitialExchangeDate"              ) var InitialExchangeDate              : String? = null
    @SerializedName("NominalInterestRate"              ) var NominalInterestRate              : String? = null
    @SerializedName("ContractDealDate"                 ) var ContractDealDate                 : String? = null
    @SerializedName("ContractID"                       ) var ContractID                       : String? = null
    @SerializedName("MaturityDate"                     ) var MaturityDate                     : String? = null
    @SerializedName("LegalEntityIDRecordCreator"       ) var LegalEntityIDRecordCreator       : String? = null
    @SerializedName("LegalEntityIDCounterparty"        ) var LegalEntityIDCounterparty        : String? = null
    @SerializedName("Tenure"                           ) var Tenure                           : String? = null
    @SerializedName("StatusDate"                       ) var StatusDate                       : String? = null
    @SerializedName("CycleAnchorDateOfInterestPayment" ) var CycleAnchorDateOfInterestPayment : String? = null
    @SerializedName("NotionalPrincipal"                ) var NotionalPrincipal                : String? = null
    @SerializedName("RoundingConvention"               ) var RoundingConvention               : String? = null
    @SerializedName("Currency"                         ) var Currency                         : String? = null
    @SerializedName("ContractType"                     ) var ContractType                     : String? = null
    @SerializedName("ContractRole"                     ) var ContractRole                     : String? = null
    @SerializedName("ContractStatus"                   ) var ContractStatus                   : String? = null
    @SerializedName("ProductIdentifier"                ) var ProductIdentifier                : String? = null
    @SerializedName("LoanCycle"                        ) var LoanCycle                        : Int?    = null
}
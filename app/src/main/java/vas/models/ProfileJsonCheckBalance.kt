package vas.models

import com.google.gson.annotations.SerializedName

data class ProfileJsonCheckBalance(

    @SerializedName("Group"         )         var Group         :        String?  = null,
    @SerializedName("branchId"      )         var branchId      :        Int?     = null,
    @SerializedName("Email"         )         var Email         :        String?  = null,
    @SerializedName("roleId"        )         var roleId        :        Int?     = null,
    @SerializedName("FirstName"     )         var FirstName     :        String? = null,
    @SerializedName("rootuserId"    )         var rootuserId    :        String?  = null,
    @SerializedName("userId"        )         var userId        :        String?  = null,
    @SerializedName("profileFormID" )         var profileFormID :        String?  = null,
    @SerializedName("Name"          )         var Name          :        String?  = null,
    @SerializedName("MobileNumber"  )         var MobileNumber  :        String? = null,
    @SerializedName("Hierarchy"     )         var Hierarchy     :        Int?     = null,
    @SerializedName("Identifier"    )         var Identifier    :        String?  = null,
    @SerializedName("Branch"        )         var Branch        :        String?  = null,
    @SerializedName("profileId"     )         var profileId     :        String?  = null,
    @SerializedName("NationalID"    )         var NationalID    :        String? = null,
    @SerializedName("FormID"        )         var FormID        :        String?  = null,
    @SerializedName("LastName"      )         var LastName      :        String? = null,
    @SerializedName("UserPassword"  )         var UserPassword  :        String? = null
)

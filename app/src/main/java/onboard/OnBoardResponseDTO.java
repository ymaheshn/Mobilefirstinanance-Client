package onboard;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OnBoardResponseDTO implements Serializable {

    public List<OnBoardResponse> onBoardList = new ArrayList<>();

    class OnBoardResponse {

        @SerializedName("ProfileID")
        public String profileID;
        @SerializedName("ownerID")
        public String ownerID;
        @SerializedName("rootUserID")
        public String rootUserID;
        @SerializedName("profileFormID")
        public String profileFormID;
        @SerializedName("status")
        public String status;
        @SerializedName("profileDetails")
        public List<ProfileDetails> profileDetails = new ArrayList<>();
        @SerializedName("profileLabel")
        public String profileLabel;
        @SerializedName("LinkedRootUserProfileID")
        public String linkedRootUserProfileID;
    }

    class ProfileDetails {

        @SerializedName("Email")
        public String email;
        @SerializedName("Address")
        public String address;
        @SerializedName("Country")
        public String country;
        @SerializedName("FormID")
        public String formID;
        @SerializedName("Gender")
        public String gender;
        @SerializedName("Last Name")
        public String lastName;
        @SerializedName("Name")
        public String name;
    }
}





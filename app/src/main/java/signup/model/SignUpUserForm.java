package signup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SignUpUserForm implements Parcelable {
    public int userFormTemplateID;
    public UserFormTemplate userFormTemplate;
    public String rootUserID;
    public String recordCreatorID;

    protected SignUpUserForm(Parcel in) {
        userFormTemplateID = in.readInt();
        rootUserID = in.readString();
        recordCreatorID = in.readString();
    }

    public static final Creator<SignUpUserForm> CREATOR = new Creator<SignUpUserForm>() {
        @Override
        public SignUpUserForm createFromParcel(Parcel in) {
            return new SignUpUserForm(in);
        }

        @Override
        public SignUpUserForm[] newArray(int size) {
            return new SignUpUserForm[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userFormTemplateID);
        dest.writeString(rootUserID);
        dest.writeString(recordCreatorID);
    }


    public static class FormHeader implements Parcelable{
        @SerializedName("Type")
        public String type;
        @SerializedName("Value")
        public String value;
        @SerializedName("Label")
        public String label;

        public FormHeader(String type, String value, String label) {
            this.type = type;
            this.value = value;
            this.label = label;
        }

        protected FormHeader(Parcel in) {
            type = in.readString();
            value = in.readString();
            label = in.readString();
        }

        public static final Creator<FormHeader> CREATOR = new Creator<FormHeader>() {
            @Override
            public FormHeader createFromParcel(Parcel in) {
                return new FormHeader(in);
            }

            @Override
            public FormHeader[] newArray(int size) {
                return new FormHeader[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(type);
            dest.writeString(value);
            dest.writeString(label);

        }
    }

    public static class FormBody implements Parcelable {
        @SerializedName("Type")
        public String type;
        @SerializedName("Value")
        public String value;
        @SerializedName("Label")
        public String label;
        @SerializedName("description")
        public String description;

        public FormBody(String type, String value, String label, String description) {
            this.type = type;
            this.value = value;
            this.label = label;
            this.description=description;
        }

        protected FormBody(Parcel in) {
            type = in.readString();
            value = in.readString();
            label = in.readString();
            description=in.readString();
        }

        public static final Creator<FormBody> CREATOR = new Creator<FormBody>() {
            @Override
            public FormBody createFromParcel(Parcel in) {
                return new FormBody(in);
            }

            @Override
            public FormBody[] newArray(int size) {
                return new FormBody[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(type);
            dest.writeString(value);
            dest.writeString(label);
            dest.writeString(description);
        }
    }

    public static class UserFormTemplate {
        @SerializedName("Form Header")
        public ArrayList<FormHeader> formHeader;
        @SerializedName("Form Body")
        public ArrayList<FormBody> formBody;
    }

}

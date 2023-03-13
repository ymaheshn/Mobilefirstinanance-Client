package shufpti;

import com.google.gson.annotations.SerializedName;

public class Name {

    @SerializedName("first_name")
    String firstName;

    @SerializedName("middle_name")
    String middleName;

    @SerializedName("last_name")
    String lastName;


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    public String getMiddleName() {
        return middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getLastName() {
        return lastName;
    }

}

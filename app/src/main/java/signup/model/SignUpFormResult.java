package signup.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class SignUpFormResult implements Map<String, String> {

    @SerializedName("Is your age above 18")
    public Object ageAbove18;
    @SerializedName("FATCA Declaration")
    public Object fatcaDeclaration;
    @SerializedName("Domicile")
    public Object domicile;
    @SerializedName("ID Verification")
    public Object idVerification;
    @SerializedName("Identifier")
    public Object identifier;
    @SerializedName("User Password")
    public Object userPassword;
    @SerializedName("First Name")
    public Object firstName;
    @SerializedName("Last Name")
    public Object lastName;
    @SerializedName("National ID")
    public Object nationalID;
    @SerializedName("Date of Birth")
    public Object dateOfBirth;
    @SerializedName("Mobile Number")
    public Object mobileNumber;
    @SerializedName("Email")
    public Object email;
    @SerializedName("Hierarchy")
    public Object hierarchy;
    @SerializedName("FormID")
    public Object formID;

    @SerializedName("Choose your plan")
    public Object choosePlan;

    public Object getAgeAbove18() {
        return ageAbove18;
    }

    public void setAgeAbove18(Object ageAbove18) {
        this.ageAbove18 = ageAbove18;
    }

    public Object getFatcaDeclaration() {
        return fatcaDeclaration;
    }

    public void setFatcaDeclaration(Object fatcaDeclaration) {
        this.fatcaDeclaration = fatcaDeclaration;
    }

    public Object getDomicile() {
        return domicile;
    }

    public void setDomicile(Object domicile) {
        this.domicile = domicile;
    }

    public Object getIdVerification() {
        return idVerification;
    }

    public void setIdVerification(Object idVerification) {
        this.idVerification = idVerification;
    }

    public Object getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Object identifier) {
        this.identifier = identifier;
    }

    public Object getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(Object userPassword) {
        this.userPassword = userPassword;
    }

    public Object getFirstName() {
        return firstName;
    }

    public void setFirstName(Object firstName) {
        this.firstName = firstName;
    }

    public Object getLastName() {
        return lastName;
    }

    public void setLastName(Object lastName) {
        this.lastName = lastName;
    }

    public Object getNationalID() {
        return nationalID;
    }

    public void setNationalID(Object nationalID) {
        this.nationalID = nationalID;
    }

    public Object getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Object dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Object getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(Object mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(Object hierarchy) {
        this.hierarchy = hierarchy;
    }

    public Object getFormID() {
        return formID;
    }

    public void setFormID(Object formID) {
        this.formID = formID;
    }

    public Object getChoosePlan() {
        return choosePlan;
    }

    public void setChoosePlan(Object choosePlan) {
        this.choosePlan = choosePlan;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(@Nullable Object key) {
        return false;
    }

    @Override
    public boolean containsValue(@Nullable Object value) {
        return false;
    }

    @Nullable
    @Override
    public String get(@Nullable Object key) {
        return null;
    }

    @Nullable
    @Override
    public String put(String key, String value) {
        return null;
    }

    @Nullable
    @Override
    public String remove(@Nullable Object key) {
        return null;
    }

    @Override
    public void putAll(@NonNull Map<? extends String, ? extends String> m) {

    }

    @Override
    public void clear() {

    }

    @NonNull
    @Override
    public Set<String> keySet() {
        return null;
    }

    @NonNull
    @Override
    public Collection<String> values() {
        return null;
    }

    @NonNull
    @Override
    public Set<Entry<String, String>> entrySet() {
        return null;
    }
}

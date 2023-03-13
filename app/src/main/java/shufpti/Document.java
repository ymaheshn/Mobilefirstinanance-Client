package shufpti;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Document {

    @SerializedName("name")
    Name name;

    @SerializedName("dob")
    String dob;

    @SerializedName("expiry_date")
    String expiryDate;

    @SerializedName("issue_date")
    String issueDate;

    @SerializedName("document_number")
    String documentNumber;

    @SerializedName("face_match_confidence")
    int faceMatchConfidence;

    @SerializedName("selected_type")
    List<String> selectedType;

    @SerializedName("supported_types")
    List<String> supportedTypes;


    public void setName(Name name) {
        this.name = name;
    }
    public Name getName() {
        return name;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
    public String getDob() {
        return dob;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
    public String getExpiryDate() {
        return expiryDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }
    public String getIssueDate() {
        return issueDate;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setFaceMatchConfidence(int faceMatchConfidence) {
        this.faceMatchConfidence = faceMatchConfidence;
    }
    public int getFaceMatchConfidence() {
        return faceMatchConfidence;
    }

    public void setSelectedType(List<String> selectedType) {
        this.selectedType = selectedType;
    }
    public List<String> getSelectedType() {
        return selectedType;
    }

    public void setSupportedTypes(List<String> supportedTypes) {
        this.supportedTypes = supportedTypes;
    }
    public List<String> getSupportedTypes() {
        return supportedTypes;
    }

}

package shufpti;

import com.google.gson.annotations.SerializedName;

public class Document_ {

    @SerializedName("selected_type")
    int selectedType;

    @SerializedName("document")
    int document;

    @SerializedName("document_must_not_be_expired")
    int documentMustNotBeExpired;

    @SerializedName("document_visibility")
    int documentVisibility;

    @SerializedName("document_proof")
    String documentProof;

    @SerializedName("face_on_document_matched")
    int faceOnDocumentMatched;

    @SerializedName("name")
    int name;

    @SerializedName("dob")
    int dob;

    @SerializedName("document_number")
    int documentNumber;

    @SerializedName("expiry_date")
    String expiryDate;

    @SerializedName("issue_date")
    String issueDate;


    public void setSelectedType(int selectedType) {
        this.selectedType = selectedType;
    }
    public int getSelectedType() {
        return selectedType;
    }

    public void setDocument(int document) {
        this.document = document;
    }
    public int getDocument() {
        return document;
    }

    public void setDocumentMustNotBeExpired(int documentMustNotBeExpired) {
        this.documentMustNotBeExpired = documentMustNotBeExpired;
    }
    public int getDocumentMustNotBeExpired() {
        return documentMustNotBeExpired;
    }

    public void setDocumentVisibility(int documentVisibility) {
        this.documentVisibility = documentVisibility;
    }
    public int getDocumentVisibility() {
        return documentVisibility;
    }

    public void setDocumentProof(String documentProof) {
        this.documentProof = documentProof;
    }
    public String getDocumentProof() {
        return documentProof;
    }

    public void setFaceOnDocumentMatched(int faceOnDocumentMatched) {
        this.faceOnDocumentMatched = faceOnDocumentMatched;
    }
    public int getFaceOnDocumentMatched() {
        return faceOnDocumentMatched;
    }

    public void setName(int name) {
        this.name = name;
    }
    public int getName() {
        return name;
    }

    public void setDob(int dob) {
        this.dob = dob;
    }
    public int getDob() {
        return dob;
    }

    public void setDocumentNumber(int documentNumber) {
        this.documentNumber = documentNumber;
    }
    public int getDocumentNumber() {
        return documentNumber;
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
}

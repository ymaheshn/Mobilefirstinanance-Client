package shufpti;

import com.google.gson.annotations.SerializedName;

public class VerificationData {

    @SerializedName("document")
    Document document;


    public void setDocument(Document document) {
        this.document = document;
    }
    public Document getDocument() {
        return document;
    }
}

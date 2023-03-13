package shufpti;

import com.google.gson.annotations.SerializedName;

public class VerificationResult {


    @SerializedName("face")
    int face;

    @SerializedName("document")
    Document_ document;


    public void setFace(int face) {
        this.face = face;
    }
    public int getFace() {
        return face;
    }

    public void setDocument(Document_ document) {
        this.document = document;
    }
    public Document_ getDocument() {
        return document;
    }

}

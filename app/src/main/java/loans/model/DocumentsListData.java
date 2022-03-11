package loans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DocumentsListData {
    @SerializedName("documents")
    @Expose
    private List<DocumentData> documents = null;

    public List<DocumentData> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentData> documents) {
        this.documents = documents;
    }
}

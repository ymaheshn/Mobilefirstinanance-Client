package dashboard.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContractJSON {

    @SerializedName("Terms")
    List<Terms> Terms;


    public void setTerms(List<Terms> Terms) {
        this.Terms = Terms;
    }
    public List<Terms> getTerms() {
        return Terms;
    }

}

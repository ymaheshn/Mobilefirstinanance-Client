package loans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Contractjson {
   /* @SerializedName("terms")
    @Expose
    public Terms terms;*/

    @SerializedName("Terms")
    @Expose
    public ArrayList<Terms> terms;


    /* ArrayList< Object > Terms = new ArrayList < Object > ();


    // Getter Methods

    public ArrayList<Object> getTerms() {
        return Terms;
    }

    public void setTerms(ArrayList<Object> terms) {
        Terms = terms;
    }*/

}

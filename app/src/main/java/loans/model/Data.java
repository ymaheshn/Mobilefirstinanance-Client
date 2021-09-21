package loans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("portfolio")
    @Expose
    public List<CollectionPortfolio> portfolio;
}

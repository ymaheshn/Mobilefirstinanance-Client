package dashboard.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApplyNewCardData {

    @SerializedName("portfolio")
    List<Portfolio> portfolio;


    public void setPortfolio(List<Portfolio> portfolio) {
        this.portfolio = portfolio;
    }

    public List<Portfolio> getPortfolio() {
        return portfolio;
    }
}

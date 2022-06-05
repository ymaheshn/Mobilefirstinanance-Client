package loans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

   /* @SerializedName("profilejson")
    @Expose
    private Profilejson profilejson;*/

   /* @SerializedName("portfolio")
    @Expose
    public Portfolio portfolioNew;*/

    @SerializedName("portfolio")
    @Expose
    public List<CollectionPortfolio> portfolio;

   /* public Profiles profiles;




    public Portfolio getPortfolio() {
        return PortfolioObject;
    }

    public Profilejson getProfilejson() {
        return profilejson;
    }

    public void setProfilejson(Profilejson profilejson) {
        this.profilejson = profilejson;
    }
// Setter Methods

    public void setPortfolio(Portfolio portfolioObject) {
        this.PortfolioObject = portfolioObject;
    }

    public Profiles getProfiles() {
        return profiles;
    }

    public void setProfiles(Profiles profiles) {
        this.profiles = profiles;
    }

    public Portfolio getPortfolioObject() {
        return PortfolioObject;
    }

    public void setPortfolioObject(Portfolio portfolioObject) {
        PortfolioObject = portfolioObject;
    }

    public void setPortfolio(List<CollectionPortfolio> portfolio) {
        this.portfolio = portfolio;
    }*/
}

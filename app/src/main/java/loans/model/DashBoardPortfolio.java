package loans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import dashboard.models.GraphCount;

public class DashBoardPortfolio {
    @SerializedName("portfolio")
    @Expose
    public GraphCount portfolio;
}

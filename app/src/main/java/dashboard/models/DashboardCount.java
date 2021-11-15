package dashboard.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DashboardCount {
    @SerializedName("workflow")
    @Expose
    public List<DashboardWorkflowCount> workflow;
}

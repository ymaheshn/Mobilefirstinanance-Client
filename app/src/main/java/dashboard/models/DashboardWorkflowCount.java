package dashboard.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardWorkflowCount {
    @SerializedName("numberofRecords")
    @Expose
    public String numberofRecords;

    @SerializedName("workflowName")
    @Expose
    public String workflowName;
}

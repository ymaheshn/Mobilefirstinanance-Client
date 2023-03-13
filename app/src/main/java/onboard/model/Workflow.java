package onboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Workflow {
    @SerializedName("workFlowID")
    @Expose
    public int workFlowID;

    @SerializedName("workFlowName")
    @Expose
    public String workFlowName;

    @SerializedName("workflowTemplates")
    @Expose
    public String workflowTemplates;

    @SerializedName("rootUserID")
    @Expose
    public String rootUserID;

    @SerializedName("ownerID")
    @Expose
    public String ownerID;

    @SerializedName("deleted")
    @Expose
    public int deleted;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("picture")
    @Expose
    public String  picture;

}

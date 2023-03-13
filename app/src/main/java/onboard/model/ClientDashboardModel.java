package onboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ClientDashboardModel{
    @SerializedName("status")
    @Expose
    public int status;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("data")
    @Expose
    public ClientDashboardModel data;

    @SerializedName("workflow")
    @Expose
    public ArrayList<Workflow> workflow;

}


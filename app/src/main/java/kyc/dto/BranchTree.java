package kyc.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BranchTree {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("branchCode")
    @Expose
    public String branchCode;
    @SerializedName("parentBranchId")
    @Expose
    public Integer parentBranchId;
    @SerializedName("currentBranchId")
    @Expose
    public Integer currentBranchId;
    @SerializedName("treeLevel")
    @Expose
    public String treeLevel;

    @SerializedName("children")
    @Expose
    public List<BranchTree> children;

    public BranchTree(String name, ArrayList<BranchTree> newList) {
        this.name = name;
        children = newList;
    }
}

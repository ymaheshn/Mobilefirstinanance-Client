package kyc.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BranchTree {
    @SerializedName("branchid")
    @Expose
    public Integer id;
    @SerializedName("branch_name")
    @Expose
    public String name;
    @SerializedName("branch_code")
    @Expose
    public String branchCode;
    @SerializedName("parent_brach_id")
    @Expose
    public Integer parentBranchId;
    @SerializedName("current_branch_id")
    @Expose
    public Integer currentBranchId;
    @SerializedName("tree_level")
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

package signup.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Branches implements Parcelable {
    private String branchName;
    private Integer currentBranchId;
    private String branchLevel;

    private ArrayList<BranchName> branchNameArrayList=new ArrayList<>();

    private ArrayList<String> branchNameList = new ArrayList<>();
    private ArrayList<Integer> currentBranchIdList = new ArrayList<>();
    private ArrayList<String> branchLevelList = new ArrayList<>();

     public Branches(String branchName, Integer currentBranchId, String branchLevel){
        this.branchName=branchName;
        this.currentBranchId=currentBranchId;
        this.branchLevel=branchLevel;
    }

    public Branches(Parcel in) {
        branchName = in.readString();
        if (in.readByte() == 0) {
            currentBranchId = null;
        } else {
            currentBranchId = in.readInt();
        }
        branchLevel = in.readString();
        branchNameList = in.createStringArrayList();
        branchLevelList = in.createStringArrayList();
    }

    public static final Creator<Branches> CREATOR = new Creator<Branches>() {
        @Override
        public Branches createFromParcel(Parcel in) {
            return new Branches(in);
        }

        @Override
        public Branches[] newArray(int size) {
            return new Branches[size];
        }
    };

    public Branches() {

    }


    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public ArrayList<BranchName> getBranchNameArrayList() {
        return branchNameArrayList;
    }

    public void setBranchNameArrayList(ArrayList<BranchName> branchNameArrayList) {
        this.branchNameArrayList = branchNameArrayList;
    }

    public Integer getCurrentBranchId() {
        return currentBranchId;
    }

    public void setCurrentBranchId(Integer currentBranchId) {
        this.currentBranchId = currentBranchId;
    }

    public String getBranchLevel() {
        return branchLevel;
    }

    public void setBranchLevel(String branchLevel) {
        this.branchLevel = branchLevel;
    }

    public ArrayList<String> getBranchNameList() {
        return branchNameList;
    }

    public void setBranchNameList(ArrayList<String> branchNameList) {
        this.branchNameList = branchNameList;
    }

    public ArrayList<Integer> getCurrentBranchIdList() {
        return currentBranchIdList;
    }

    public void setCurrentBranchIdList(ArrayList<Integer> currentBranchIdList) {
        this.currentBranchIdList = currentBranchIdList;
    }

    public ArrayList<String> getBranchLevelList() {
        return branchLevelList;
    }

    public void setBranchLevelList(ArrayList<String> branchLevelList) {
        this.branchLevelList = branchLevelList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(branchName);
        if (currentBranchId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(currentBranchId);
        }
        dest.writeString(branchLevel);
        dest.writeStringList(branchNameList);
        dest.writeStringList(branchLevelList);
    }
}

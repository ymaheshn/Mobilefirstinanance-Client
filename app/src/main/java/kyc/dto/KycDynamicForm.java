package kyc.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gufran khan on 23-06-2018.
 */

public class KycDynamicForm implements Serializable {
    public String profileFormId = "";
    public String ownerID = "";
    public String rootUserID = "";
    public  String formName="";
    public ArrayList<ProfileForm> allProfileFormsList = new ArrayList<>();
}

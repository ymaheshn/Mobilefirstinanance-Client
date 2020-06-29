package onboard;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gufran khan on 20-10-2018.
 */

public class TabDto implements Serializable {
    public int tabId;
    public String tabName = "";
    public ArrayList<TabFields> tabFieldsArrayList = new ArrayList<>();
    public ArrayList<TabFields> tabFieldsBodyArrayList = new ArrayList<>();


}

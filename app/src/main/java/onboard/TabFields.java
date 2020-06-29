package onboard;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gufran khan on 20-10-2018.
 */

public class TabFields implements Serializable {
    public String name = "";
    public String type = "";
    public String value = "";
    public ArrayList<String> valuesList = new ArrayList<>();

}

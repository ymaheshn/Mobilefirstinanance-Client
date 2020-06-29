package kyc.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gufran khan on 23-06-2018.
 */

public class Profileform implements Serializable {
    public String name = "";
    public String type = "";
    public ArrayList<String> value = new ArrayList<>();
}

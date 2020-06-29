package onboard;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gufran khan on 04-11-2018.
 */

public class WorkFlowTemplateDto implements Serializable{
    public String workFlowId="";
    public String workFlowName="";
    public String workFlowProfileId="";
    public ArrayList<TabDto> tabDtoArrayList=new ArrayList<>();
}

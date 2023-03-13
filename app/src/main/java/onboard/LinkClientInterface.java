package onboard;

import java.util.ArrayList;

import onboard.model.Workflow;

public interface LinkClientInterface {

    void linkClient(int position,ArrayList<Workflow> data);

    void onItemClick(int position, ArrayList<Workflow> data);
}

package onboard;

import java.util.ArrayList;

import onboard.model.Workflow;

public interface OnBoardClientAdapterItemClickListener {

    void onItemClick(int position, ArrayList<Workflow> data);
}

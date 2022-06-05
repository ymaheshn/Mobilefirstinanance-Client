package savings;

import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.odedtech.mff.mffapp.R;

import base.BaseFragment;

public class SavingsFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_savings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RadioGroup savingsRadio = view.findViewById(R.id.radio_savings);

        RecyclerView rvTransactions = view.findViewById(R.id.rv_transactions);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvTransactions.setLayoutManager(linearLayoutManager);
        rvTransactions.setAdapter(new TransactionsAdapter(getActivity(), datum -> {

        }));


        RecyclerView rvTransfers = view.findViewById(R.id.rv_transfers);
        LinearLayoutManager transfersLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvTransfers.setLayoutManager(transfersLayoutManager);
        rvTransfers.setAdapter(new TransfersAdapter(getActivity(), datum -> {

        }));

        View containerTransfers = view.findViewById(R.id.container_transfers);
        View containerTransactions = view.findViewById(R.id.container_transactions);
        savingsRadio.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.radio_transactions) {
                containerTransfers.setVisibility(View.GONE);
                containerTransactions.setVisibility(View.VISIBLE);
            } else if (i == R.id.radio_transfers) {
                containerTransactions.setVisibility(View.GONE);
                containerTransfers.setVisibility(View.VISIBLE);
            }
        });
    }
}
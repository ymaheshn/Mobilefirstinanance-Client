package vas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.odedtech.mff.mffapp.R;

import base.BaseFragment;

public class VasFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vas_new, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.card_transfer).setOnClickListener(view1 -> {
            startActivity(new Intent(getActivity(), TransferActivity.class));
        });

        view.findViewById(R.id.card_block).setOnClickListener(view1 -> {
            startActivity(new Intent(getActivity(), BlockCardActivity.class));
        });
    }
}

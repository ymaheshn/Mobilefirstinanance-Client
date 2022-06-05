package vas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.odedtech.mff.mffapp.R;

import Utilities.AlertDialogUtils;
import Utilities.PreferenceConnector;
import base.BaseFragment;
import login.LoginActivity;

public class VasFragment extends BaseFragment {
    public AppCompatButton logoutButton;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vas_new, container, false);
        logoutButton = view.findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setMessage("Are you sure, You want to logout?");
            alertDialogBuilder.setPositiveButton("yes", (dialog, which) -> {
                callLogout();
            });

            alertDialogBuilder.setNegativeButton("No", (dialog, which) -> {
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });
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

    private void callLogout() {
        PreferenceConnector.clearPref(getContext());
        Intent intent = new Intent(view.getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}

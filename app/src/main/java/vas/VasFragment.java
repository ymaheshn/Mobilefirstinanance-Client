package vas;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import com.odedtech.mff.client.R;

import Utilities.PreferenceConnector;
import base.BaseFragment;
import login.LoginActivity;

public class VasFragment extends BaseFragment implements View.OnClickListener {
    public CardView logoutButton, checkBalanceButton;
    private View view;
    private ScrollView scrollView;
    private int colorCode;
    private AppCompatImageView checkBalIcon, airTimeIcon, transferIcon, paybillsIcon, buyDataIcon, blockCardIcon, logoutIcon;
    private AppCompatTextView checkBalText, airTimeText, transferText, payBillsText, buyDataText, blockCardText, logoutText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vas_new, container, false);
        logoutButton = view.findViewById(R.id.card_logout);
        checkBalanceButton = view.findViewById(R.id.cardView_check_balance);
        scrollView = view.findViewById(R.id.scroll_services);

        String colorTheme = PreferenceConnector.getThemeColor(getContext());
        colorCode = Color.parseColor(colorTheme);

        scrollView.setBackgroundColor(colorCode);

        checkBalanceButton.setOnClickListener(this);

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
            startActivity(new Intent(requireContext(), TransferActivity.class));
        });

        view.findViewById(R.id.card_block).setOnClickListener(view1 -> {
            startActivity(new Intent(requireContext(), BlockCardActivity.class));
        });

        //ImageIcons
        checkBalIcon = view.findViewById(R.id.checkBalIcon);
        checkBalIcon.setColorFilter(colorCode);
        airTimeIcon = view.findViewById(R.id.airTimeIcon);
        airTimeIcon.setColorFilter(colorCode);
        transferIcon = view.findViewById(R.id.transferIcon);
        transferIcon.setColorFilter(colorCode);
        paybillsIcon = view.findViewById(R.id.paybill_icon);
        paybillsIcon.setColorFilter(colorCode);
        buyDataIcon = view.findViewById(R.id.buy_data_icon);
        buyDataIcon.setColorFilter(colorCode);
        blockCardIcon = view.findViewById(R.id.block_cardIcon);
        blockCardIcon.setColorFilter(colorCode);
        logoutIcon = view.findViewById(R.id.logOutIcon);
        logoutIcon.setColorFilter(colorCode);

        checkBalText = view.findViewById(R.id.text_checkBal);
        checkBalText.setTextColor(colorCode);
        airTimeText = view.findViewById(R.id.airtime_text);
        airTimeText.setTextColor(colorCode);
        transferText = view.findViewById(R.id.transferText);
        transferText.setTextColor(colorCode);
        payBillsText = view.findViewById(R.id.paybillText);
        payBillsText.setTextColor(colorCode);
        buyDataText = view.findViewById(R.id.buy_data_text);
        buyDataText.setTextColor(colorCode);
        blockCardText = view.findViewById(R.id.block_card_text);
        blockCardText.setTextColor(colorCode);
        logoutText = view.findViewById(R.id.logout_text);
        logoutText.setTextColor(colorCode);
    }

    private void callLogout() {
        PreferenceConnector.clearPref(getContext());
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cardView_check_balance) {
            Intent intent = new Intent(requireContext(), CheckBalanceActivity.class);
            startActivity(intent);
        }
    }
}

package dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.odedtech.mff.client.R;
import com.odedtech.mff.client.databinding.ActivityApplyNewCardBinding;

import Utilities.AlertDialogUtils;
import Utilities.PreferenceConnector;
import Utilities.ProgressBar;
import dashboard.adapter.LinkedCardsListAdapter;
import dashboard.models.ApplyNewCardResponse;
import dashboard.models.CardDetailsList;
import network.MFFApiWrapper;
import onboard.LinkCardInterface;
import onboard.model.ApplyCardResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplyNewCardActivity extends AppCompatActivity implements LinkCardInterface {
    public ApplyNewCardResponse applyNewCardResponse;
    public LinkedCardsListAdapter linkedCardsListAdapter;
    private ActivityApplyNewCardBinding binding;
    private String linkedProfileId;
    private CardDetailsList cardDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityApplyNewCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        linkedProfileId = intent.getStringExtra("linkedProfileId");

        ProgressBar.showProgressDialog(this);

        getLinkedCardDetails();

        binding.icBackApplyCard.setOnClickListener(v -> onBackPressed());
        binding.buttonApply.setOnClickListener(v -> applyButtonPressed());


    }

    private void getLinkedCardDetails() {
        String accessToken = PreferenceConnector.readString(getApplicationContext(), getString(R.string.accessToken), "");
        MFFApiWrapper.getInstance().service.getLinkCardDetails(linkedProfileId, accessToken).enqueue(new Callback<ApplyNewCardResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApplyNewCardResponse> call, @NonNull Response<ApplyNewCardResponse> response) {
                ProgressBar.dismissDialog();
                if (response.code() == 200 && response.body() != null) {
                    applyNewCardResponse = response.body();
                    setDataToAdapter(applyNewCardResponse);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApplyNewCardResponse> call, @NonNull Throwable t) {
                ProgressBar.dismissDialog();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setDataToAdapter(ApplyNewCardResponse applyNewCardResponse) {
        linkedCardsListAdapter = new LinkedCardsListAdapter(getApplicationContext(), applyNewCardResponse, linkedProfileId, this, this);
        binding.contractsListRecyclerView.setAdapter(linkedCardsListAdapter);
    }

    @Override
    public void getCardLink(CardDetailsList cardDetails, int position) {
        this.cardDetailsList = cardDetails;
        if (cardDetails.getCardDetails().size()>0) {
            binding.buttonApply.setEnabled(true);
            binding.buttonApply.setBackgroundColor(getColor(R.color.green_color_gradient));
        }else {
            binding.buttonApply.setEnabled(false);
            binding.buttonApply.setBackgroundColor(getColor(R.color.gray));
        }

    }

    private void applyButtonPressed() {
        ProgressBar.showProgressDialog(this);
        String accessToken = PreferenceConnector.readString(getApplicationContext(), getString(R.string.accessToken), "");
        if (cardDetailsList.getCardDetails().size()>0) {
            MFFApiWrapper.getInstance().service.applyCard(accessToken, cardDetailsList).enqueue(new Callback<ApplyCardResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApplyCardResponse> call, @NonNull Response<ApplyCardResponse> response) {
                    ProgressBar.dismissDialog();
                    if (response.code() == 200 && response.body() != null) {
                        Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_LONG).show();
                        AlertDialogUtils.getAlertDialogUtils().showSuccessAlert(ApplyNewCardActivity.this, getString(R.string.card_applied_successfully));
                    } else if (response.code() == 500) {
                        Toast.makeText(getApplicationContext(), R.string.internal_server_error, Toast.LENGTH_LONG).show();
                    } else if (response.code() == 412) {
                        AlertDialogUtils.getAlertDialogUtils().showAlreadyAppliedAlert(ApplyNewCardActivity.this, getString(R.string.card_already_applied));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApplyCardResponse> call, @NonNull Throwable t) {
                    ProgressBar.dismissDialog();
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else {
            ProgressBar.dismissDialog();
            Toast.makeText(getApplicationContext(), "Please select card", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
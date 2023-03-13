package loans;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.odedtech.mff.client.R;
import com.odedtech.mff.client.databinding.FragmentBusinessDocumentsBinding;

import java.util.List;

import Utilities.PreferenceConnector;
import base.BaseActivity;
import loans.model.BusinessDocumentsModel;
import loans.model.DocumentData;
import loans.model.DocumentsAdapter;
import network.MFFApiWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessDocumentsActivity extends BaseActivity {

    private FragmentBusinessDocumentsBinding binding;
    private String profileId;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentBusinessDocumentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        profileId = getIntent().getStringExtra("profileId");
        context = this;
        getBusinessDocuments();
        binding.ivBack.setOnClickListener(v -> onBackPressed());
    }


    private void getBusinessDocuments() {
        binding.progressBar.setVisibility(View.VISIBLE);

        String accessToken = PreferenceConnector.readString(context,
                getString(R.string.accessToken), "");
        MFFApiWrapper.getInstance().service.getBusinessDocuments(profileId, accessToken).enqueue(new Callback<BusinessDocumentsModel>() {
            @Override
            public void onResponse(@NonNull Call<BusinessDocumentsModel> call,
                                   @NonNull Response<BusinessDocumentsModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    loadDocuments(response.body());
                } else {
                    checkResponseError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BusinessDocumentsModel> call, @NonNull Throwable t) {
                dismissLoading();
                Toast.makeText(context,
                        getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDocuments(BusinessDocumentsModel body) {
        if (body.getData().getDocuments() != null && body.getData().getDocuments().size()>0) {
            binding.textLoadDate.setVisibility(View.GONE);
            binding.rvList.setVisibility(View.VISIBLE);

            List<DocumentData> documentsList=body.getData().getDocuments();
            DocumentsAdapter documentsAdapter =new DocumentsAdapter(context,documentsList);
            binding.rvList.setLayoutManager(new LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false));
            binding.rvList.setAdapter(documentsAdapter);

        } else {
            binding.textLoadDate.setVisibility(View.VISIBLE);
            binding.rvList.setVisibility(View.GONE);
        }
    }

}

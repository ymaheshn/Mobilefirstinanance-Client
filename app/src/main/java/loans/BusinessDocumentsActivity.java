package loans;

import Utilities.PreferenceConnector;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;
import base.BaseActivity;
import com.odedtech.mff.mffapp.R;
import com.odedtech.mff.mffapp.databinding.FragmentBusinessDocumentsBinding;
import loans.model.BusinessDocumentsModel;
import loans.model.DocumentData;
import loans.model.DocumentsAdapter;
import network.MFFApiWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

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
            public void onResponse(Call<BusinessDocumentsModel> call,
                                   Response<BusinessDocumentsModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    loadDocuments(response.body());
                } else {
                    checkResponseError(response);
                }
            }

            @Override
            public void onFailure(Call<BusinessDocumentsModel> call, Throwable t) {
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

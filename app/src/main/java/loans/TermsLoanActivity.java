package loans;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.odedtech.mff.client.R;

import java.util.ArrayList;
import java.util.List;

import Utilities.PreferenceConnector;
import base.BaseActivity;
import loans.model.TermsDataDTO;
import loans.model.LoansPortfolio;
import loans.model.LoansPortfolioResponse;
import loans.model.Terms;
import network.MFFApiWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsLoanActivity extends BaseActivity {

    public TextView productName, loadId, loanAmount, disbursementDate, maturityDate, interestDate;
    public ImageView termsBackImage;
    private String contractUUID;
    private LoansPortfolioResponse termsData = new LoansPortfolioResponse();
    private List<LoansPortfolio> loansPortfolios = new ArrayList<>();
    /*  private Profilejson profilejson;
      private Contractjson contractjson;*/
    private ArrayList<Terms> terms;
    private TermsDataDTO termsDataDTO;
    private ProgressBar progressBar;
    private AppCompatTextView toolBarTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_loan);

        productName = findViewById(R.id.product_name_text);
        loadId = findViewById(R.id.loan_id_text);
        loanAmount = findViewById(R.id.loan_amount_text);
        disbursementDate = findViewById(R.id.disbursement_date_text);
        maturityDate = findViewById(R.id.maturity_date_text);
        interestDate = findViewById(R.id.interest_date_text);
        termsBackImage = findViewById(R.id.back_terms_image);
        progressBar = findViewById(R.id.progress_bar);
        toolBarTextView = findViewById(R.id.tv_title);

        String colorTheme = PreferenceConnector.getThemeColor(getApplicationContext());
        int colorCode = Color.parseColor(colorTheme);
        termsBackImage.setColorFilter(colorCode);
        toolBarTextView.setTextColor(colorCode);

        Intent intent = getIntent();
        contractUUID = intent.getStringExtra("contractUUID");
        termsBackImage.setOnClickListener(v -> onBackPressed());
        progressBar.setVisibility(View.VISIBLE);
        getTermsDetailsFromAPI();
    }

    private void getTermsDetailsFromAPI() {
        String accessToken = PreferenceConnector.readString(getApplicationContext(),
                this.getString(R.string.accessToken), "");
        MFFApiWrapper.getInstance().service.getTermsDetails(contractUUID, accessToken, 0, 10).
                enqueue(new Callback<TermsDataDTO>() {
                    @Override
                    public void onResponse(@NonNull Call<TermsDataDTO> call, @NonNull Response<TermsDataDTO> response) {
                        progressBar.setVisibility(View.VISIBLE);
                        if (response.code() == 200 && response.body() != null) {
                            progressBar.setVisibility(View.GONE);
                            termsDataDTO = response.body();
                            setDataToView(termsDataDTO);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<TermsDataDTO> call, @NonNull Throwable t) {
                        Log.d("result", t.getMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void setDataToView(TermsDataDTO termsDataDTO) {
        productName.setText(termsDataDTO.data.portfolioNew.ContractjsonObject.terms.get(0).contractID);
        loadId.setText(termsDataDTO.data.portfolioNew.ContractjsonObject.terms.get(0).contractID);
        loanAmount.setText(termsDataDTO.data.portfolioNew.ContractjsonObject.terms.get(0).notionalPrincipal);
        disbursementDate.setText(termsDataDTO.data.portfolioNew.ContractjsonObject.terms.get(0).initialExchangeDate);
        maturityDate.setText(termsDataDTO.data.portfolioNew.ContractjsonObject.terms.get(0).maturityDate);
        interestDate.setText(termsDataDTO.data.portfolioNew.ContractjsonObject.terms.get(0).nominalInterestRate);
    }
}
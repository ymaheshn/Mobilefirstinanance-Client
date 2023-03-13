package dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.odedtech.mff.client.R;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Utilities.AlertDialogUtils;
import Utilities.PreferenceConnector;
import Utilities.ProgressBar;
import base.BaseFragment;
import base.MFFResponseNew;
import dashboard.adapter.DashboardDetailsAdapter;
import dashboard.models.GraphCount;
import dashboard.models.ProfileCount;
import interfaces.ApplyCardClickListener;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import loans.model.DashBoardGraphResponse;
import network.MFFApiWrapper;
import onboard.model.CardDetailsData;
import onboard.model.CreditCardResponse;
import onboard.model.ProfileDetailsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends BaseFragment implements ApplyCardClickListener {

    private PieChartView chart;
    public PieChartData data;

    public boolean hasLabels = false;
    public boolean hasLabelsOutside = false;
    public boolean hasCenterCircle = true;
    public boolean hasCenterText1 = false;
    public boolean hasCenterText2 = false;
    public boolean isExploded = false;
    public boolean hasLabelForSelected = false;
    public DashboardDetailsAdapter detailsAdapter;
    private TextView textTotalClients;
    private View graphProgress;
    private View dashProgress;
    private RecyclerView listDashboardDetails;
    private View textPar;
    private ProfileDetailsResponse profileDetailsResponse;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chart = view.findViewById(R.id.piechart);
        chart.setOnValueTouchListener(new ValueTouchListener());
        textTotalClients = view.findViewById(R.id.text_total_clients);
        textPar = view.findViewById(R.id.text_par);
        graphProgress = view.findViewById(R.id.graph_progress);
        dashProgress = view.findViewById(R.id.dashboardProgress);
        listDashboardDetails = view.findViewById(R.id.list_dashboard_details);

        getProfileCount();
        getLinkProfileId();
        getCardDetailsApi();
        // getDashboardDetailsCount();
        getGraphDetails();


    }

    private void getLinkProfileId() {
        String accessToken = PreferenceConnector.readString(getContext(), getString(R.string.accessToken), "");
        MFFApiWrapper.getInstance().service.getLinkedProfileId(0, 10, accessToken).enqueue(new Callback<ProfileDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProfileDetailsResponse> call, @NonNull Response<ProfileDetailsResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    profileDetailsResponse = response.body();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProfileDetailsResponse> call, @NonNull Throwable t) {

            }
        });
    }

    private void generateData(GraphCount body) {
        List<SliceValue> values = new ArrayList<>();
        SliceValue sliceValue30 = new SliceValue(body.value30);
        sliceValue30.setColor(Color.parseColor("#D73C2C"));
        values.add(sliceValue30);
        SliceValue sliceValue60 = new SliceValue(body.value60);
        sliceValue60.setColor(Color.parseColor("#6E248D"));
        values.add(sliceValue60);
        SliceValue sliceValue120 = new SliceValue(body.value120);
        sliceValue120.setColor(Color.parseColor("#0067B0"));
        values.add(sliceValue120);
        SliceValue sliceValue180 = new SliceValue(body.value180);
        sliceValue180.setColor(Color.parseColor("#106B60"));
        values.add(sliceValue180);
        SliceValue sliceValue240 = new SliceValue(body.value240);
        sliceValue240.setColor(Color.parseColor("#F39C12"));
        values.add(sliceValue240);
        SliceValue sliceValue360 = new SliceValue(body.value360);
        sliceValue360.setColor(Color.parseColor("#E3724B"));
        values.add(sliceValue360);
        SliceValue sliceValueGreateer360 = new SliceValue(body.value360);
        sliceValueGreateer360.setColor(Color.parseColor("#6C7A89"));
        values.add(sliceValueGreateer360);
        data = new PieChartData(values);
        data.setHasLabels(hasLabels);
        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data.setHasLabelsOutside(hasLabelsOutside);
        data.setHasCenterCircle(hasCenterCircle);
        if (isExploded) {
            data.setSlicesSpacing(24);
        }

        if (hasCenterText1) {
            data.setCenterText1("Hello!");

            // Get roboto-italic font.
            Typeface tf = Typeface.createFromAsset(requireActivity().getAssets(), "Roboto-Italic.ttf");
            data.setCenterText1Typeface(tf);

            // Get font size from dimens.xml and convert it to sp(library uses sp values).
            data.setCenterText1FontSize(14);
        }

        if (hasCenterText2) {
            data.setCenterText2("Charts (Roboto Italic)");

            Typeface tf = Typeface.createFromAsset(requireActivity().getAssets(), "Roboto-Italic.ttf");

            data.setCenterText2Typeface(tf);
            data.setCenterText2FontSize(12);
        }

        chart.setPieChartData(data);
        chart.setCircleFillRatio(10);
    }

 /*   private void explodeChart() {
        isExploded = !isExploded;
        generateData(null);
    }*/

   /* private void toggleLabelsOutside() {
        // has labels have to be true:P
        hasLabelsOutside = !hasLabelsOutside;
        if (hasLabelsOutside) {
            hasLabels = true;
            hasLabelForSelected = false;
            chart.setValueSelectionEnabled(hasLabelForSelected);
        }

        if (hasLabelsOutside) {
            chart.setCircleFillRatio(0.7f);
        } else {
            chart.setCircleFillRatio(1.0f);
        }

        generateData(null);

    }*/

   /* private void toggleLabels() {
        hasLabels = !hasLabels;

        if (hasLabels) {
            hasLabelForSelected = false;
            chart.setValueSelectionEnabled(hasLabelForSelected);

            if (hasLabelsOutside) {
                chart.setCircleFillRatio(0.7f);
            } else {
                chart.setCircleFillRatio(1.0f);
            }
        }

        generateData(null);
    }*/

   /* private void toggleLabelForSelected() {
        hasLabelForSelected = !hasLabelForSelected;

        chart.setValueSelectionEnabled(hasLabelForSelected);

        if (hasLabelForSelected) {
            hasLabels = false;
            hasLabelsOutside = false;

            if (hasLabelsOutside) {
                chart.setCircleFillRatio(0.7f);
            } else {
                chart.setCircleFillRatio(1.0f);
            }
        }

        generateData(null);
    }*/

    /**
     * method(don't confuse with View.animate()).
     */
 /*   private void prepareDataAnimation() {
        for (SliceValue value : data.getValues()) {
            value.setTarget((float) Math.random() * 30 + 15);
        }
    }*/

    @Override
    public void applyCardClicked() {
        String linkedProfileId = profileDetailsResponse.getData().getProfiles().get(0).getLinkedProfileID();
        Intent intent = new Intent(requireContext(), ApplyNewCardActivity.class);
        intent.putExtra("linkedProfileId", linkedProfileId);
        startActivity(intent);


    }

    private static class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            //Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }
    }

    private void getCardDetailsApi() {
        ProgressBar.showProgressDialog(requireActivity());
        dashProgress.setVisibility(View.VISIBLE);
        String accessToken = PreferenceConnector.readString(requireContext(), getString(R.string.accessToken), "");
        String status = "active";
        try {
            MFFApiWrapper.getInstance().service.getCardDetails(status, accessToken).enqueue(new Callback<CreditCardResponse>() {
                @Override
                public void onResponse(@NonNull Call<CreditCardResponse> call, @NonNull Response<CreditCardResponse> response) {
                    dashProgress.setVisibility(View.GONE);
                    ProgressBar.dismissDialog();
                    try {
                        if (response.body() != null && response.code() == 200) {
                            setToAdapter(response.body());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ProgressBar.dismissDialog();
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(@NonNull Call<CreditCardResponse> call, @NonNull Throwable t) {
                    ProgressBar.dismissDialog();
                    dashProgress.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("MaheshNaidu", t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            ProgressBar.dismissDialog();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void setToAdapter(CreditCardResponse creditCardResponse) {
        CardDetailsData detailsList = creditCardResponse.getData();
        detailsAdapter = new DashboardDetailsAdapter(requireContext(), detailsList, this);
        listDashboardDetails.setAdapter(detailsAdapter);
    }

    private void getGraphDetails() {
        graphProgress.setVisibility(View.VISIBLE);
        chart.setVisibility(View.GONE);
        textPar.setVisibility(View.GONE);
        SimpleDateFormat formatter =  new SimpleDateFormat("yyyy-MM-dd");
        String strDate = formatter.format(new Date());
        String accessToken = PreferenceConnector.readString(requireContext(), getString(R.string.accessToken), "");
        MFFApiWrapper.getInstance().service.getGraphDetails(strDate, accessToken).enqueue(new Callback<DashBoardGraphResponse>() {
            @Override
            public void onResponse(@NonNull Call<DashBoardGraphResponse> call, @NonNull Response<DashBoardGraphResponse> response) {
                if (response.body() != null) {
                    DashBoardGraphResponse body = response.body();
                    GraphCount objCount = body.data.portfolio;
                    graphProgress.setVisibility(View.GONE);
                    if (objCount != null) {
                        chart.setVisibility(View.VISIBLE);
                        textPar.setVisibility(View.VISIBLE);
                        generateData(objCount);
                    } else {
                        Toast.makeText(getActivity(),
                                getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    graphProgress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DashBoardGraphResponse> call, @NonNull Throwable t) {
                graphProgress.setVisibility(View.GONE);
                Toast.makeText(getActivity(),
                        getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProfileCount() {
        String accessToken = PreferenceConnector.readString(getActivity(), getString(R.string.accessToken), "");
        MFFApiWrapper.getInstance().service.getTotalClient(accessToken).enqueue(new Callback<MFFResponseNew<ProfileCount>>() {
            @Override
            public void onResponse(@NonNull Call<MFFResponseNew<ProfileCount>> call, @NonNull Response<MFFResponseNew<ProfileCount>> response) {
                MFFResponseNew<ProfileCount> body = response.body();
                if (body != null && body.status == HttpURLConnection.HTTP_OK) {
                    if (body.data != null) {
                        textTotalClients.setText(String.valueOf(body.data.value));
                    }
                } else {
                    Toast.makeText(getActivity(),
                            getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    AlertDialogUtils.getAlertDialogUtils().showAlert(getActivity());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MFFResponseNew<ProfileCount>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(),
                        getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
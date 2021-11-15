package dashboard;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.odedtech.mff.mffapp.R;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Utilities.PreferenceConnector;
import base.BaseFragment;
import base.MFFResponseNew;
import dashboard.models.DashboardCount;
import dashboard.models.GraphCount;
import dashboard.models.ProfileCount;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import network.MFFApiWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends BaseFragment {

    private PieChartView chart;
    private PieChartData data;

    private boolean hasLabels = false;
    private boolean hasLabelsOutside = false;
    private boolean hasCenterCircle = true;
    private boolean hasCenterText1 = false;
    private boolean hasCenterText2 = false;
    private boolean isExploded = false;
    private boolean hasLabelForSelected = false;
    private DashboardDetailsAdapter detailsAdapter;
    private TextView textTotalClients;
    private View graphProgress;
    private View dashProgress;
    private RecyclerView listDashboardDetails;
    private View textPar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chart = (PieChartView) view.findViewById(R.id.piechart);
        chart.setOnValueTouchListener(new ValueTouchListener());
        textTotalClients = view.findViewById(R.id.text_total_clients);
        textPar = view.findViewById(R.id.text_par);
        graphProgress = view.findViewById(R.id.graph_progress);
        dashProgress = view.findViewById(R.id.dashboardProgress);
        setUpRecyclerview(view);
        getProfileCount();
        getDashboardDetailsCount();
        getGraphDetails();
    }

    private void setUpRecyclerview(View view) {
        detailsAdapter = new DashboardDetailsAdapter(getActivity());
        listDashboardDetails = view.findViewById(R.id.list_dashboard_details);
        listDashboardDetails.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL));
        listDashboardDetails.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        listDashboardDetails.setAdapter(detailsAdapter);
    }

    private void generateData(GraphCount body) {
        List<SliceValue> values = new ArrayList<SliceValue>();
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
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");
            data.setCenterText1Typeface(tf);

            // Get font size from dimens.xml and convert it to sp(library uses sp values).
            data.setCenterText1FontSize(14);
        }

        if (hasCenterText2) {
            data.setCenterText2("Charts (Roboto Italic)");

            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");

            data.setCenterText2Typeface(tf);
            data.setCenterText2FontSize(12);
        }

        chart.setPieChartData(data);
        chart.setCircleFillRatio(10);
    }

    private void explodeChart() {
        isExploded = !isExploded;
        generateData(null);
    }

    private void toggleLabelsOutside() {
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

    }

    private void toggleLabels() {
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
    }

    private void toggleLabelForSelected() {
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
    }

    /**
     * method(don't confuse with View.animate()).
     */
    private void prepareDataAnimation() {
        for (SliceValue value : data.getValues()) {
            value.setTarget((float) Math.random() * 30 + 15);
        }
    }

    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            //Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }
    }


    private void getGraphDetails() {
        graphProgress.setVisibility(View.VISIBLE);
        chart.setVisibility(View.GONE);
        textPar.setVisibility(View.GONE);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = formatter.format(new Date());
        String accessToken = PreferenceConnector.readString(getActivity(), getString(R.string.accessToken), "");
        MFFApiWrapper.getInstance().service.getGraphDetails(strDate, accessToken).enqueue(new Callback<GraphCount>() {
            @Override
            public void onResponse(Call<GraphCount> call, Response<GraphCount> response) {
                GraphCount body = response.body();
                graphProgress.setVisibility(View.GONE);
                if (body != null) {
                    chart.setVisibility(View.VISIBLE);
                    textPar.setVisibility(View.VISIBLE);
                    generateData(body);
                } else {
                    Toast.makeText(getActivity(),
                            getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GraphCount> call, Throwable t) {
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
            public void onResponse(Call<MFFResponseNew<ProfileCount>> call, Response<MFFResponseNew<ProfileCount>> response) {
                MFFResponseNew<ProfileCount> body = response.body();
                if (body != null && body.status == HttpURLConnection.HTTP_OK) {
                    if (body.data != null) {
                        textTotalClients.setText(String.valueOf(body.data.value));
                    }
                } else {
                    Toast.makeText(getActivity(),
                            getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MFFResponseNew<ProfileCount>> call, Throwable t) {
                Toast.makeText(getActivity(),
                        getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDashboardDetailsCount() {
        dashProgress.setVisibility(View.VISIBLE);
        listDashboardDetails.setVisibility(View.INVISIBLE);
        String accessToken = PreferenceConnector.readString(getActivity(), getString(R.string.accessToken), "");
        MFFApiWrapper.getInstance().service.getOnBoardDetailsCount(accessToken).enqueue(new Callback<MFFResponseNew<DashboardCount>>() {
            @Override
            public void onResponse(Call<MFFResponseNew<DashboardCount>> call, Response<MFFResponseNew<DashboardCount>> response) {
                dashProgress.setVisibility(View.GONE);
                MFFResponseNew<DashboardCount> body = response.body();
                if (body != null && body.status == HttpURLConnection.HTTP_OK) {
                    listDashboardDetails.setVisibility(View.VISIBLE);
                    detailsAdapter.setData(body.data.workflow);
                } else {
                    Toast.makeText(getActivity(),
                            getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MFFResponseNew<DashboardCount>> call, Throwable t) {
                dashProgress.setVisibility(View.GONE);
                Toast.makeText(getActivity(),
                        getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
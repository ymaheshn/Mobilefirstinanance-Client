package addclient;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.odedtech.mff.mffapp.R;

import Utilities.Constants;
import adapters.AddClientViewPagerAdapter;
import base.BaseFragment;
import base.ProfileDetailsFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dashboard.DashboardActivity;
import interfaces.IOnDataRetrievedFromGallery;
import interfaces.IOnFragmentChangeListener;
import kyc.KycFragment;
import onboard.TabDto;
import onboard.WorkFlowTemplateDto;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddClientFragment extends BaseFragment implements TabLayout.OnTabSelectedListener, IOnDataRetrievedFromGallery {
    public static final String KEY_FORM_ID = "form_id";

    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.kycIndicatorIV)
    ImageView kycIndicatorIV;
    @BindView(R.id.cashFlowIndicatorIV)
    ImageView cashFlowIndicatorIV;
    @BindView(R.id.creditBureauIndicatorIV)
    ImageView creditBureauIndicatorIV;
    @BindView(R.id.creditScoreIndicatorIV)
    ImageView creditScoreIndicatorIV;
    @BindView(R.id.approvalIndicatorIV)
    ImageView approvalIndicatorIV;
    @BindView(R.id.addClientContentFL)
    FrameLayout addClientContentFL;
    @BindView(R.id.kycTV)
    TextView kycTV;
    @BindView(R.id.cashFlowTV)
    TextView cashFlowTV;
    @BindView(R.id.creditBureauTV)
    TextView creditBureauTV;
    @BindView(R.id.creditScoreTV)
    TextView creditScoreTV;
    @BindView(R.id.ApprovalTV)
    TextView ApprovalTV;
    @BindView(R.id.tablayoutLL)
    LinearLayout tablayoutLL;
    private Unbinder unbinder;
    public WorkFlowTemplateDto workFlowTemplateDt = null;
    private IOnFragmentChangeListener iOnFragmentChangeListener;
    private int formId;
    private AddClientViewPagerAdapter adapter;

    public AddClientFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_client, container, false);
        // bind view using butter knife
        unbinder = ButterKnife.bind(this, view);
        workFlowTemplateDt = Constants.workFlowTemplateDt;
        if (getArguments() != null) {
            formId = getArguments().getInt(KEY_FORM_ID);
        }
        return view;
    }

    private void replaceFragmentToContent(Fragment fragment, String tagName) {
        getChildFragmentManager().beginTransaction()
                .addToBackStack(null)
                .add(R.id.addClientContentFL, fragment, tagName)
                .commit();

        //set indicators
        if (fragment instanceof KycFragment) {
            clearAllIndications();
            kycIndicatorIV.setImageDrawable(getResources().getDrawable(R.drawable.tab_indicator_pink));
            kycTV.setTextColor(getResources().getColor(R.color.colorDarkPink));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((DashboardActivity) getActivity()).setOnDataRetrievedFromGallery(this);
        iOnFragmentChangeListener = (IOnFragmentChangeListener) getActivity();
        //KycFragment kycFragment = new KycFragment();
        //replaceFragmentToContent(kycFragment, "");
        tabLayout.addOnTabSelectedListener(this);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        KycFragment kycFragment = new KycFragment();
        kycFragment.setArguments(getArguments());
        if (Constants.isFromAddClient) {
            iOnFragmentChangeListener.onHeaderUpdate(Constants.KYC_FRAGMENT, "KYC");
            tablayoutLL.setVisibility(View.GONE);
            adapter = new AddClientViewPagerAdapter(getChildFragmentManager());
            adapter.addFragment(kycFragment, "KYC");
            viewPager.setAdapter(adapter);
        } else {
            iOnFragmentChangeListener.onHeaderUpdate(Constants.CASH_FLOW_FRAGMENT, "KYC");
            tablayoutLL.setVisibility(View.VISIBLE);
            adapter = new AddClientViewPagerAdapter(getChildFragmentManager());
            if (workFlowTemplateDt != null && workFlowTemplateDt.tabDtoArrayList != null) {
                int index = 0;
                for (TabDto tabDto : workFlowTemplateDt.tabDtoArrayList) {
                    if (tabDto.tabName.equalsIgnoreCase(Constants.KYC)) {
                        kycFragment.setTabIndex(index);
                        adapter.addFragment(kycFragment, Constants.KYC);
                    } else {
                        adapter.addFragment(new ProfileDetailsFragment(index, tabDto.tabName), tabDto.tabName);
                    }
                    index++;
                }
            }
            viewPager.setAdapter(adapter);
        }
    }

    private boolean containsTab(String tabName) {
        boolean status = false;
        for (TabDto tabDto : workFlowTemplateDt.tabDtoArrayList) {
            if (tabDto.tabName.equalsIgnoreCase(tabName)) {
                status = true;
                break;
            }

        }
        return status;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // unbind the view to free some memory
        unbinder.unbind();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        clearAllIndications();
        if (tab.getText().equals(Constants.KYC)) {
            iOnFragmentChangeListener.onHeaderUpdate(Constants.KYC_FRAGMENT, "KYC");
            kycIndicatorIV.setImageDrawable(getResources().getDrawable(R.drawable.tab_indicator_pink));
            kycTV.setTextColor(getResources().getColor(R.color.colorDarkPink));
        } else {
            iOnFragmentChangeListener.onHeaderUpdate(Constants.PROFILE_DETAILS_FRAGMENT, tab.getText().toString());
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void clearAllIndications() {
        kycIndicatorIV.setImageDrawable(getResources().getDrawable(R.drawable.tab_indicator_gray));
        cashFlowIndicatorIV.setImageDrawable(getResources().getDrawable(R.drawable.tab_indicator_gray));
        creditBureauIndicatorIV.setImageDrawable(getResources().getDrawable(R.drawable.tab_indicator_gray));
        creditScoreIndicatorIV.setImageDrawable(getResources().getDrawable(R.drawable.tab_indicator_gray));
        approvalIndicatorIV.setImageDrawable(getResources().getDrawable(R.drawable.tab_indicator_gray));

        kycTV.setTextColor(getResources().getColor(R.color.colorWhite));
        cashFlowTV.setTextColor(getResources().getColor(R.color.colorWhite));
        creditBureauTV.setTextColor(getResources().getColor(R.color.colorWhite));
        creditScoreTV.setTextColor(getResources().getColor(R.color.colorWhite));
        ApprovalTV.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public void onDataRetrieved(int requestCode, int resultCode, Intent data) {
        int currentItem = viewPager.getCurrentItem();
        Fragment fragment = adapter.getItem(currentItem);
        if (fragment instanceof IOnDataRetrievedFromGallery) {
            ((IOnDataRetrievedFromGallery) fragment).onDataRetrieved(requestCode, resultCode, data);
        }
    }
}

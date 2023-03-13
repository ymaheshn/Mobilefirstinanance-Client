package signup.listener;

import android.view.View;

import adapters.SignUpViewPagerAdapter;

public interface OnContinueButtonClickListener {
    void onClick(View view, int position, String branchName, String label, Object value, SignUpViewPagerAdapter.ViewPagerHolder viewPagerHolder,boolean isFromBranch,boolean isFromDateOFBirth,boolean isFromIdVerification,boolean isFromSkip);
}

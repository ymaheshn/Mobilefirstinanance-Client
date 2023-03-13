package signup.listener;

import java.util.ArrayList;

import adapters.SignUpViewPagerAdapter;

public interface SignUpDetailsInterface {

    void getSignUpData(ArrayList<String> signUpDataList, SignUpViewPagerAdapter.ViewPagerHolder viewPagerHolder, int position, String s, String enteredEdittext);

}

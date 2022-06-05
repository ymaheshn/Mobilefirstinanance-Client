package base;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.odedtech.mff.mffapp.R;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import Utilities.UtilityMethods;
import retrofit2.Response;

public class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void showLoading() {
        ((BaseActivity) requireActivity()).showLoading();
    }

    public void dismissLoading() {
        ((BaseActivity) requireActivity()).dismissLoading();
    }

    protected void showDatePickerDialog(EditText editText) {
        // Get Current Date

        int mYear = 0, mMonth = 0, mDay = 0;
        String date = editText.getText().toString();
        if (TextUtils.isEmpty(date)) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        } else {
            String[] split = date.split("-");
            if (split.length == 3) {
                mYear = Integer.parseInt(split[0]);
                mMonth = Integer.parseInt(split[1]) - 1;
                mDay = Integer.parseInt(split[2]);
            }
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    String month = "" + (monthOfYear + 1);
                    if (monthOfYear < 10) {
                        month = "0" + (monthOfYear + 1);
                    }
                    String day = "" + (dayOfMonth);
                    if (dayOfMonth < 10) {
                        day = "0" + (dayOfMonth);
                    }
                    editText.setText(UtilityMethods.getDateFormat(day + "" + month + "" + year, UtilityMethods.KYC_DATE_FORMAT));
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    protected void checkResponseError(Response response) {
        if (response.code() == 401) {
            try {
                MFFErrorResponse errorResponse = new Gson()
                        .fromJson(response.errorBody().string(), MFFErrorResponse.class);
                if (errorResponse.error != null && errorResponse.error.equals("invalid_token")) {
                    ((BaseActivity) getActivity()).navigateToLogin();
                }
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(getActivity(),
                getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
    }
}

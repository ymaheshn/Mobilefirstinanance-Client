package client.login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import base.BaseActivity
import com.odedtech.mff.mffapp.R
import dashboard.DashboardActivity
import kotlinx.android.synthetic.main.activity_pin.*
import networking.WebService
import networking.WebServiceURLs

class PinActivity : BaseActivity(), WebService.OnServiceResponseListener {

    private var otpValue: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)
        val preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val pinValue = preferences.getString("key_pin", null)
        text_desc.visibility = if (TextUtils.isEmpty(pinValue)) View.VISIBLE else View.GONE
        otp_view.setOtpCompletionListener {
            otpValue = it
        }
    }


    private fun verifyOtp() {
        showLoading()
        //post parameters
//        params.put("mobileNumber", phoneNumber);
        val url = WebServiceURLs.PHONENUMBER_OTP + "?phoneNumber=" + "phoneNumber" + "&otp=" + otpValue
        WebService.getInstance().apiGetRequestCall(url, this)
    }

    fun nextBTNClick(view: View) {
        val preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val pinValue = preferences.getString("key_pin", null)
        if (TextUtils.isEmpty(pinValue)) {
            if (otpValue?.length!! < 4) {
                Toast.makeText(this@PinActivity, "Please enter 4 digit pin", Toast.LENGTH_LONG).show()
                return
            }
            preferences.edit().putString("key_pin", otpValue).commit()
            finish()
            startActivity(Intent(this@PinActivity, DashboardActivity::class.java))
        } else {
            if (pinValue.equals(otpValue)) {
                finish()
                startActivity(Intent(this@PinActivity, DashboardActivity::class.java))
            } else {
                Toast.makeText(this@PinActivity, "Wrong 4 digit pin", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onApiCallResponseSuccess(url: String?, `object`: String?) {


    }

    override fun onApiCallResponseFailure(errorMessage: String?) {
    }
}

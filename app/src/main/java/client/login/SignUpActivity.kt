package client.login

import Utilities.PreferenceConnector
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import base.BaseActivity
import com.odedtech.mff.client.R
import kotlinx.android.synthetic.main.activity_client_login.*
import kotlinx.android.synthetic.main.activity_registration.*
import networking.WebService
import networking.WebServiceURLs
import org.json.JSONException
import org.json.JSONObject
import otp.OTPActivity
import java.util.*

class SignUpActivity : BaseActivity(), WebService.OnServiceResponseListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this, R.layout.item_spinner, android.R.id.text1, arrayListOf("Male", "Female"))
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_gender.adapter = spinnerArrayAdapter
    }

    fun nextBtnClicked(view: View) {
        showLoading()
        val params = HashMap<String, String>()
        params["First Name"] = etFirstName.text.toString()
        params["mobileNumber"] = etPhone.text.toString()
        params["Gender"] = spinner_gender.selectedItem as String
        params["Identifier"] = etUserName.text.toString()
//        params["FormID"] = numberET.text.toString()
        params["Last Name"] = etLastName.text.toString()
        WebService.getInstance().apiPostRequestCall(WebServiceURLs.CLIENT_USER_CREATION_URL,
                params, this)
    }

    override fun onApiCallResponseSuccess(url: String?, `object`: String?) {
        dismissLoading()
        try {
            val jsonObject = JSONObject(`object`)
            if (jsonObject.has("status") && !TextUtils.isEmpty(jsonObject.getString("status")) && jsonObject.getString("status").equals("success", ignoreCase = true)) {
                if (!TextUtils.isEmpty(numberET.text.toString()) && numberET.text.toString().length == 10) {
                    PreferenceConnector.writeString(applicationContext, getString(R.string.phoneNumber), numberET.text.toString())
                    val intent = Intent(this, OTPActivity::class.java)
                    intent.putExtra(getString(R.string.number_bundle), numberET.text.toString())
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Something went wrong. Please try after some time.", Toast.LENGTH_SHORT).show()
                }
            } else if (jsonObject.has("message") && !TextUtils.isEmpty(jsonObject.getString("message"))) {
                Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Could't start session.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Toast.makeText(this, getString(R.string.something_went_erong), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onApiCallResponseFailure(errorMessage: String?) {
        dismissLoading()
        Toast.makeText(this, getString(R.string.something_went_erong), Toast.LENGTH_SHORT).show()
    }
}

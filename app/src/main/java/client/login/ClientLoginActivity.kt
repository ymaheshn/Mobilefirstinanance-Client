package client.login

import Utilities.PreferenceConnector
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import base.BaseActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.odedtech.mff.client.R
import kotlinx.android.synthetic.main.activity_client_login.*
import networking.WebService
import networking.WebServiceURLs
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class ClientLoginActivity : BaseActivity(), WebService.OnServiceResponseListener {

    override fun onApiCallResponseSuccess(url: String?, `object`: String?) {
        dismissLoading()
        try {
            val jsonObject = JSONObject(`object`)
            if (jsonObject.has("status") && !TextUtils.isEmpty(jsonObject.getString("status")) && jsonObject.getString("status").equals("success", ignoreCase = true)) {
                if (!TextUtils.isEmpty(numberET.text.toString()) && numberET.text.toString().length == 10) {
                    PreferenceConnector.writeString(applicationContext, getString(R.string.phoneNumber), numberET.text.toString())
                    val intent = Intent(this, ClientOtpScrren::class.java)
                    intent.putExtra(getString(R.string.number_bundle), numberET.text.toString())
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Something went wrong. Please try after some time.", Toast.LENGTH_SHORT).show()
                }
            } else if (jsonObject != null && jsonObject.has("message") && !TextUtils.isEmpty(jsonObject.getString("message"))) {
                Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Couldn't start session.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Toast.makeText(this, "Something went wrong. Please try after some time.", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onApiCallResponseFailure(errorMessage: String?) {
        dismissLoading()
        Toast.makeText(this, "Something went wrong. Please try after some time.", Toast.LENGTH_SHORT).show()
    }

    private val RC_SIGN_IN: Int = 10
    private val TAG = "ClientLoginActivity"
    lateinit var signInClient: GoogleSignInClient

//    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_login)

//        FacebookSdk.sdkInitialize(getApplicationContext());
//        // Google signin
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build()
//
//        signInClient = GoogleSignIn.getClient(this, gso)
//
//        //Facebook sign in
//        callbackManager = CallbackManager.Factory.create()
//
//        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
//            override fun onSuccess(loginResult: LoginResult) {
//                finish()
//                startActivity(Intent(this@ClientLoginActivity, ClientOTPActivity::class.java))
//            }
//
//            override fun onCancel() {
//
//            }
//
//            override fun onError(error: FacebookException) {
//
//            }
//        })

        container_signup.setOnClickListener {
            startActivity(Intent(this@ClientLoginActivity, SignUpActivity::class.java))
        }

    }

    fun googleSignIn(view: View) {
        val signInIntent = signInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignIn(task)
        }
    }

    fun facebookSignIn(view: View) {
//        LoginManager.getInstance()
//                .logInWithReadPermissions(this, Arrays.asList("public_profile, emao;"))
    }

    private fun handleSignIn(task: Task<GoogleSignInAccount>) {
        try {
            val result = task.getResult(ApiException::class.java)
            Log.v(TAG, "signInResult:Email=" + result?.email)
            finish()
            startActivity(Intent(this@ClientLoginActivity, ClientOTPActivity::class.java))
        } catch (e: ApiException) {
            Log.v(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    fun nextBtnClicked(view: View) {
        showLoading()
        val params = HashMap<String, String>()
        params["mobileNumber"] = numberET.text.toString()
        WebService.getInstance().apiPostRequestCall(WebServiceURLs.CLIENT_LOGIN_URL, params, this)
    }

}

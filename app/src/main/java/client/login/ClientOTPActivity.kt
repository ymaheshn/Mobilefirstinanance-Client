package client.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.odedtech.mff.mffapp.R

class ClientOTPActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_otp)
    }

    fun nextBTNClick(view: View) {
        finish()
        startActivity(Intent(this@ClientOTPActivity, PinActivity::class.java))
    }

}

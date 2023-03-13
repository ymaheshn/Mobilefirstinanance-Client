package vas

import Utilities.PreferenceConnector
import Utilities.ProgressBar
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.odedtech.mff.client.R
import com.odedtech.mff.client.databinding.ActivityCheckBalanceBinding
import interfaces.ApiCallResponseListener
import network.ApiService
import onboard.model.ProfileDetailsResponse
import retrofit2.Response
import vas.adapter.CheckBalanceAdapter
import vas.models.CheckBalanceData
import vas.models.CheckBalanceResponse

class CheckBalanceActivity : AppCompatActivity(), ApiCallResponseListener {

    private lateinit var binding: ActivityCheckBalanceBinding
    private var apiService: ApiService? = null
    private var checkBalanceData: CheckBalanceData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckBalanceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val colorTheme = PreferenceConnector.getThemeColor(applicationContext)
        val colorCode = Color.parseColor(colorTheme)
        binding.toolbarText.setTextColor(colorCode)
        binding.icBack.setColorFilter(colorCode)

        apiService = ApiService(this, this@CheckBalanceActivity)
        val accessToken: String? =
            PreferenceConnector.readString(applicationContext, getString(R.string.accessToken), "")
        ProgressBar.showProgressDialog(this)
        apiService?.getLinkedProfileId(0, 10, accessToken)

        binding.icBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onSuccess(responseData: Response<*>?) {
        ProgressBar.dismissDialog()
        responseData?.let { it ->
            it.body()?.let { it ->
                if (it is ProfileDetailsResponse) {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                    checkBalanceData(it.data.profiles[0].linkedProfileID)
                } else if (it is CheckBalanceResponse) {

                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                    checkBalanceData = it.data
                    binding.rvCheckBalance.adapter =
                        checkBalanceData?.let { CheckBalanceAdapter(applicationContext, it) }
                }
            }
        }
    }

    override fun onFailure(error: String?) {
        ProgressBar.dismissDialog()
        Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
    }

    private fun checkBalanceData(linkedProfileId: String) {
        ProgressBar.showProgressDialog(this)
        val accessToken: String? =
            PreferenceConnector.readString(applicationContext, getString(R.string.accessToken), "")
        apiService?.checkBalance(linkedProfileId, accessToken)

    }
}
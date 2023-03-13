package vas

import Utilities.AlertDialogUtils
import Utilities.PreferenceConnector
import Utilities.ProgressBar
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.odedtech.mff.client.R
import com.odedtech.mff.client.databinding.ActivityBlockCardBinding
import interfaces.ApiCallResponseListener
import network.ApiService
import network.MFFApiWrapper
import onboard.model.CreditCardResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vas.models.BlockCardsPayload
import vas.models.BlockedCardDetailsList
import vas.models.CardBlockStatusResponse


class BlockCardActivity : AppCompatActivity(),
    IOnCheckBoxBlockCardClickListener, ApiCallResponseListener {

    private val dashProgress: View? = null
    private lateinit var binding: ActivityBlockCardBinding
    private lateinit var cardResponse: CreditCardResponse

    private var blockCardsPayload = BlockCardsPayload()

    var cardDetailsList: List<BlockedCardDetailsList> = ArrayList()
    var apiService: ApiService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlockCardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val colorTheme = PreferenceConnector.getThemeColor(applicationContext)
        val colorCode = Color.parseColor(colorTheme)
        binding.toolBarText.setTextColor(colorCode)
        binding.icBack.setColorFilter(colorCode)

        apiService = ApiService(this, this@BlockCardActivity)

        binding.icBack.setOnClickListener {
            finish()
        }
        binding.blockCardButton.setOnClickListener {
            this.blockCardsPayload
            blockCardsApi(blockCardsPayload)
        }
        getCardDetailsApi()
    }


    private fun getCardDetailsApi() {
        ProgressBar.showProgressDialog(this)
        dashProgress?.visibility = View.VISIBLE
        val accessToken: String? =
            PreferenceConnector.readString(applicationContext, getString(R.string.accessToken), "")
        val status = "active"
        try {
            MFFApiWrapper.getInstance().service.getCardDetails(status, accessToken)
                .enqueue(object : Callback<CreditCardResponse?> {
                    override fun onResponse(
                        call: Call<CreditCardResponse?>,
                        response: Response<CreditCardResponse?>
                    ) {
                        ProgressBar.dismissDialog()
                        dashProgress?.visibility = View.GONE
                        try {
                            if (response.body() != null && response.code() == 200) {
                                binding.blockCardButton.visibility = View.VISIBLE
                                setToAdapter(response.body()!!)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<CreditCardResponse?>, t: Throwable) {
                        ProgressBar.dismissDialog()
                        dashProgress?.visibility = View.GONE
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
            ProgressBar.dismissDialog()
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
        }
    }


    private fun setToAdapter(creditCardResponse: CreditCardResponse) {
        cardResponse = creditCardResponse
        if (cardResponse.data.cardDetails.size > 0) {
            binding.rvAccounts.adapter =
                AccountsBlockAdapter(applicationContext, creditCardResponse, this)
        } else {
            binding.noDataText.visibility = View.VISIBLE
            binding.blockCardButton.visibility = View.GONE
            binding.textAccountName.visibility = View.GONE
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_enter_pin)
        val button = dialog.findViewById<Button>(R.id.btn_submit)
        button.setOnClickListener {
            dialog.dismiss()
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to block?")
            builder.setPositiveButton("Confirm") { dialog, id ->
                val pDialog = ProgressDialog(this)
                pDialog.setMessage("Loading..")
                pDialog.setCancelable(false)
                pDialog.setCanceledOnTouchOutside(false)
                pDialog.show()


                Handler().postDelayed({
                    pDialog.dismiss()
                    val builder2 = AlertDialog.Builder(this)
                    builder2.setMessage("Your card is successfully blocked")
                    builder2.setPositiveButton("OK") { dialog, id ->
                        finish()
                    }
                    val alert11s = builder2.create()
                    alert11s.setCanceledOnTouchOutside(false)
                    alert11s.setCancelable(false)
                    alert11s.show()
                }, 3000)
            }
            builder.setNegativeButton("Cancel", null)
            val alert11 = builder.create()
            alert11.setCanceledOnTouchOutside(false)
            alert11.setCancelable(false)
            alert11.show()
        }
        dialog.show()
    }

    override fun onCheckBoxItemClicked(
        position: Int,
        cardDetailsList: MutableList<BlockedCardDetailsList>?
    ) {
        if (cardDetailsList?.size!! >0) {
            binding.blockCardButton.background =getDrawable(R.drawable.apply_button)
            cardDetailsList.let {
                blockCardsPayload.cardDetailsList = cardDetailsList
            }
        } else {
            binding.blockCardButton.background = getDrawable(R.drawable.apply_button_grey)

        }
    }

    private fun blockCardsApi(blockCardsPayload: BlockCardsPayload) {
        this.blockCardsPayload = blockCardsPayload
        val accessToken: String? =
            PreferenceConnector.readString(applicationContext, getString(R.string.accessToken), "")
        if (blockCardsPayload.cardDetailsList.size > 0) {
            apiService?.blockCards(applicationContext, accessToken, blockCardsPayload)
            binding.blockCardButton.setBackgroundColor(getColor(R.color.green_color_gradient))
        } else {
            Toast.makeText(applicationContext, getString(R.string.select_card), Toast.LENGTH_LONG).show()
        }
    }

    override fun onSuccess(responseData: Response<*>?) {
        responseData?.let { it ->
            it.body()?.let {
                if (it is CardBlockStatusResponse) {
                    AlertDialogUtils.getAlertDialogUtils().showSuccessBlockCardAlert(this, it.message)
                    Toast.makeText(applicationContext, it.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onFailure(error: String?) {
        Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
    }


}
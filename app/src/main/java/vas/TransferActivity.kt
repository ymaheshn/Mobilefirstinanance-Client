package vas

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.odedtech.mff.client.R
import kotlinx.android.synthetic.main.activity_transfer.*


class TransferActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer)
        val backButton = findViewById<ImageView>(R.id.ic_back)

        backButton.setOnClickListener {
            finish()
        }

        val textAmount = findViewById<EditText>(R.id.text_amount)
        textAmount.filters = arrayOf<InputFilter>(MinMaxFilter("1", "1000000"))
        textAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (text_amount.text.toString().isNotEmpty()) {
                    val output =
                        Currency.convertToIndianCurrency(text_amount.text.toString().trim())
                    text_words.text = output
                    text_words.visibility = View.VISIBLE
                } else {
                    text_words.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })


        val editText = findViewById<TextInputEditText>(R.id.editText)
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (editText.text?.length!! >= 10) {
                    text_account_name.visibility = View.VISIBLE
                } else {
                    text_account_name.visibility = View.INVISIBLE
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        findViewById<Button>(R.id.transfer).setOnClickListener {
            showDialog()
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
            builder.setMessage("Are you sure you want to transfer?")
            builder.setPositiveButton("Confirm") { dialog, id ->
                val pDialog = ProgressDialog(this)
                pDialog.setMessage("Loading..")
                pDialog.setCancelable(false)
                pDialog.setCanceledOnTouchOutside(false)
                pDialog.show()


                Handler().postDelayed({
                    pDialog.dismiss()
                    val builder2 = AlertDialog.Builder(this)
                    builder2.setMessage("Amount has been transferred successfully")
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

    inner class MinMaxFilter() : InputFilter {
        private var intMin: Int = 0
        private var intMax: Int = 0

        constructor(minValue: String, maxValue: String) : this() {
            this.intMin = Integer.parseInt(minValue)
            this.intMax = Integer.parseInt(maxValue)
        }

        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dStart: Int,
            dEnd: Int
        ): CharSequence? {
            try {
                val input = Integer.parseInt(dest.toString() + source.toString())
                if (isInRange(intMin, intMax, input)) {
                    return null
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            return ""
        }

        private fun isInRange(a: Int, b: Int, c: Int): Boolean {
            return if (b > a) c in a..b else c in b..a
        }
    }
}
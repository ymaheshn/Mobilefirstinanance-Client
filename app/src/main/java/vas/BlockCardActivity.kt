package vas

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.odedtech.mff.mffapp.R

class BlockCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block_card)
        val backButton = findViewById<ImageView>(R.id.ic_back)

        backButton.setOnClickListener {
            finish()
        }

        val rvTransactions: RecyclerView = findViewById(R.id.rv_accounts)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvTransactions.layoutManager = linearLayoutManager
        rvTransactions.adapter = AccountsBlockAdapter(this,
                null)

        findViewById<Button>(R.id.block_card).setOnClickListener {
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
}
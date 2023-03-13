package loans

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.odedtech.mff.client.databinding.ActivityTransactionHistoryBinding
import loans.model.LoansPortfolio

class TransactionHistoryActivity : AppCompatActivity() {

    private lateinit var items: List<LoansPortfolio>
    private lateinit var binding: ActivityTransactionHistoryBinding
    private lateinit var loanPortPolio: LoansPortfolio
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionHistoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        items = intent.getParcelableArrayListExtra("loanData")!!
        position = intent.getIntExtra("position", 0)
        loanPortPolio = items[position]

        val openingBal = "Opening balance"
        val pR = "Principal amount"
        val interestAmount = "Interest amount"
        val fee = "Fee"
        if (loanPortPolio.event_type == "IED") {
            binding.textTransfer.text = openingBal
        }
        if (loanPortPolio.event_type == "PR") {
            binding.textTransfer.text = pR
        }
        if (loanPortPolio.event_type == "IP") {
            binding.textTransfer.text = interestAmount
        }
        if (loanPortPolio.event_type == "FP") {
            binding.textTransfer.text = fee
        }

        binding.dateOneTv.text = loanPortPolio.event_time.subSequence(0, 10)
      //  binding.transferAmount.text = loanPortPolio.eventJSON.transaction.value.toString()

      //  binding.currencyType.text = loanPortPolio.eventJSON.transaction.units


        //  binding.recyclerView.adapter = TransactionHistoryAdapter(applicationContext,items, loanPortPolio)
    }
}
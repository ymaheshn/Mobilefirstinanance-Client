package loans

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.odedtech.mff.client.databinding.TransactionHistoryItemsBinding
import loans.model.LoansPortfolio

class TransactionHistoryAdapter(
    var context: Context?,
    var items: List<LoansPortfolio>,
    private var loanPortPolio: LoansPortfolio
) : RecyclerView.Adapter<TransactionHistoryAdapter.MyViewHolder>() {

    class MyViewHolder internal constructor(val binding: TransactionHistoryItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TransactionHistoryItemsBinding
            .inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val openingBal = "Opening balance"
        val pR = "Principal amount"
        val interestAmount = "Interest amount"
        val fee = "Fee"
        if (loanPortPolio.event_type == "IED") {
            holder.binding.textTransfer.text = openingBal
        }
        if (loanPortPolio.event_type == "PR") {
            holder.binding.textTransfer.text = pR
        }
        if (loanPortPolio.event_type == "IP") {
            holder.binding.textTransfer.text = interestAmount
        }
        if (loanPortPolio.event_type == "FP") {
            holder.binding.textTransfer.text = fee
        }
        holder.binding.dateOneTv.text = loanPortPolio.event_time.subSequence(0, 11)
        holder.binding.transferAmount.text = loanPortPolio.eventJSON.transaction.value.toString()
        holder.binding.currencyType.text = loanPortPolio.eventJSON.transaction.units

    }

    override fun getItemCount(): Int {
        return 0
    }
}
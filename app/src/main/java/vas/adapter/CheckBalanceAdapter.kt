package vas.adapter

import Utilities.PreferenceConnector
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.odedtech.mff.client.R
import kotlinx.android.synthetic.main.check_balance_view_item.view.*
import vas.models.CheckBalanceData

class CheckBalanceAdapter(
    private val context: Context,
    private val checkBalanceData: CheckBalanceData
) :
    RecyclerView.Adapter<CheckBalanceAdapter.MyViewHolder>() {

    class MyViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productName: AppCompatTextView? = null
        var productNameTv: AppCompatTextView? = null
        var contractID: AppCompatTextView? = null
        var contractIDTv: AppCompatTextView? = null
        var notionalPrincipal: AppCompatTextView? = null
        var notionalPrincipalTv: AppCompatTextView? = null

        init {
            productName = itemView.product_name
            productNameTv = itemView.product_name_tv
            contractID = itemView.contract_id
            contractIDTv = itemView.contract_id_tv
            notionalPrincipal = itemView.notional_principle
            notionalPrincipalTv = itemView.notional_principle_tv
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.check_balance_view_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.productName?.text = checkBalanceData.portfolio[position].productName.toString()
        holder.contractID?.text = checkBalanceData.portfolio[position].contractID.toString()
        holder.notionalPrincipal?.text =
            checkBalanceData.portfolio[position].contractJSON?.Terms?.get(0)?.NotionalPrincipal

        val colorTheme = PreferenceConnector.getThemeColor(context)
        val colorCode = Color.parseColor(colorTheme)
        holder.productNameTv?.setTextColor(colorCode)
        holder.contractIDTv?.setTextColor(colorCode)
        holder.notionalPrincipalTv?.setTextColor(colorCode)
    }

    override fun getItemCount(): Int {
        return checkBalanceData.portfolio.size
    }

}
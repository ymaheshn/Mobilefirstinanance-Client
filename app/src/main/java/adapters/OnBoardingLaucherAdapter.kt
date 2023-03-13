package adapters

import adapters.OnBoardingLaucherAdapter.MyViewHolder
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import base.OnBoardingItemDTO
import com.odedtech.mff.client.R
import kotlinx.android.synthetic.main.on_board_launcher_items.view.*

class OnBoardingLaucherAdapter(
    val applicationContext: Context,
    private val onboardingItemDTOS: MutableList<OnBoardingItemDTO>
) : RecyclerView.Adapter<MyViewHolder>() {


    class MyViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var headerText: AppCompatTextView? = null
        var subText: AppCompatTextView? = null
        var image: AppCompatImageView? = null

        init {
            headerText = itemView.text_view_header
            subText = itemView.sub_text
            image = itemView.image_on_board_view
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(applicationContext).inflate(R.layout.on_board_launcher_items, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.headerText?.text=onboardingItemDTOS[position].headerTitle
        holder.subText?.text=onboardingItemDTOS[position].subTitle
        holder.image?.background=onboardingItemDTOS[position].image
    }

    override fun getItemCount(): Int {
       return onboardingItemDTOS.size
    }

}
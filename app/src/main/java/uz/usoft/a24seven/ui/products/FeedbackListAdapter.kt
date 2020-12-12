package uz.usoft.a24seven.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_product.view.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.network.di.MockData

class FeedbackListAdapter(): RecyclerView.Adapter<FeedbackListAdapter.ViewHolder>() {

    var productsList: List<MockData.ProductObject>? = MockData.getFeedbackList()

    fun updateList(productsList: List<MockData.ProductObject>) {
        this.productsList = productsList
        notifyDataSetChanged()
    }


    var onItemClick: ((MockData.ProductObject) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
         LayoutInflater.from(parent.context).inflate(R.layout.item_feedback, parent, false)
    )

    override fun getItemCount() = productsList?.size ?: 0

    override fun onBindViewHolder(holder: FeedbackListAdapter.ViewHolder, position: Int) {
        holder.bindData(productsList!![position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(productsList!![adapterPosition])
            }
        }

        fun bindData(product: MockData.ProductObject) {
        }
    }
}

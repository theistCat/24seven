package uz.usoft.a24seven.ui.profile.myOrders

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.ItemOrderItemBinding
import uz.usoft.a24seven.network.models.OrderItem
import uz.usoft.a24seven.utils.image

class OrderItemListAdapter(val context:Context) : RecyclerView.Adapter<OrderItemListAdapter.ViewHolder>() {

    var productsList: List<OrderItem>? = null

    fun updateList(productsList: List<OrderItem>) {
        this.productsList = productsList
        notifyDataSetChanged()
    }


    var onItemClick: ((OrderItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = productsList?.size ?: 0

    override fun onBindViewHolder(holder: OrderItemListAdapter.ViewHolder, position: Int) {
        holder.bindData(productsList!![position])
    }

    inner class ViewHolder(var bindin: ItemOrderItemBinding) : RecyclerView.ViewHolder(bindin.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(productsList!![bindingAdapterPosition])
            }
        }

        fun bindData(product:OrderItem) {
                bindin.productName.text=product.name
                bindin.categoryImage.image(context,product.image.path_thumb)
                bindin.productCount.text=context.getString(R.string.count_with_unit_,product.count,product.unit.name)
                bindin.productPrice.text=context.getString(R.string.money_format_sum_string,product.price)
        }
    }
}
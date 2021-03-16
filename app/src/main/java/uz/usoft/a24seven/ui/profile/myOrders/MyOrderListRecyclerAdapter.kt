package uz.usoft.a24seven.ui.profile.myOrders

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.android.synthetic.main.item_order.view.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.ItemOrderBinding
import uz.usoft.a24seven.network.di.MockData

class MyOrderListRecyclerAdapter(val orderListType: String = "") :
    RecyclerView.Adapter<MyOrderListRecyclerAdapter.ViewHolder>() {

    var productsList: List<MockData.ProductObject>? = MockData.getFeedbackList()

    fun updateList(productsList: List<MockData.ProductObject>) {
        this.productsList = productsList
        notifyDataSetChanged()
    }


    var onItemClick: ((MockData.ProductObject) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = productsList?.size ?: 0

    override fun onBindViewHolder(holder: MyOrderListRecyclerAdapter.ViewHolder, position: Int) {
        holder.bindData(productsList!![position])
    }

    inner class ViewHolder(var binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {


        init {
            itemView.orderDetails.setOnClickListener {
                onItemClick?.invoke(productsList!![adapterPosition])
            }
        }

        fun bindData(product: MockData.ProductObject) {
            val binding = binding as ItemOrderBinding
            when (orderListType) {
                "active" -> {
                    binding.orderStatus.setTextColor(Color.parseColor("#1BC06D"))
                    binding.orderStatus.text = "Активные"
                }
                "inactive" -> {
                    binding.orderStatus.setTextColor(Color.parseColor("#F8B068"))
                    binding.orderStatus.text = "Ожидание"
                }
                "delivered" -> {
                    binding.orderStatus.setTextColor(Color.parseColor("#DB3022"))
                    binding.orderStatus.text = "Доставлен"
                }
            }
        }
    }
}

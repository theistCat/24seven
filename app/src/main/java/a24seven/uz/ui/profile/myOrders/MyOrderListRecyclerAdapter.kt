package a24seven.uz.ui.profile.myOrders

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import a24seven.uz.R
import a24seven.uz.databinding.ItemOrderBinding
import a24seven.uz.network.models.Order
import a24seven.uz.network.utils.Variables


class MyOrderListRecyclerAdapter(
    val context: Context,
    val orderListType: String = "",
    val viewModel: OrdersViewModel
) :
    PagingDataAdapter<Order, MyOrderListRecyclerAdapter.ViewHolder>(ORDER) {

    var onItemClick: ((Order) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(getItem(position) as Order)
    }


    inner class ViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.orderDetails.setOnClickListener {
                onItemClick?.invoke(getItem(bindingAdapterPosition) as Order)
            }
        }

        fun bindData(order: Order) {

            binding.orderStatus.setTextColor(
                when (orderListType) {
//                    Variables.orderType[1]!! -> {
//                        Color.parseColor("#1BC06D")
//                    }

                    Variables.orderType[0]!! -> {
                        Color.parseColor("#F8B068")
                    }

//                    Variables.orderType[2]!! -> {
//                        Color.parseColor("#DB3022")
//                    }
//
                    else -> {
                        Color.parseColor("#DB3022")
                    }
                }
            )

            binding.orderStatus.text =
                when (orderListType) {
                    Variables.orderType[0]!! -> {
                        context.getString(R.string.in_wait)
                    }

                    else -> {
                        context.getString(R.string.active)
                    }

//                    Variables.orderType[2]!! -> {
//                        context.getString(R.string.delivered)
//                    }
//
//                    else -> {
//                        context.getString(R.string.active)
//                    }
                }


            if (order.status == Variables.orderType[3])
                binding.orderStatus.text = context.getString(R.string.canceled)


            binding.orderDate.text = order.created_at
            binding.orderID.text = context.getString(R.string.order_number, order.id)
            binding.orderItemCount.text = order.products_count.toString()
            binding.orderPrice.text = context.getString(
                R.string.money_format_sum,
                order.price_products + order.price_delivery
            )
            when (orderListType) {
                Variables.orderType[1]!! -> {
//                    viewModel.addOne((order.price_products + order.price_delivery).toInt())
                    viewModel.setPriceList1((order.price_products + order.price_delivery).toInt())
                    viewModel.setDateList1(order.created_at)
//                    viewModel.setTypeList(order.status)
                }

                Variables.orderType[2]!! -> {
//                    viewModel.addOne((order.price_products + order.price_delivery).toInt())
                    viewModel.setPriceList2((order.price_products + order.price_delivery).toInt())
                    viewModel.setDateList2(order.created_at)
//                    viewModel.setTypeList(order.status)
                }

                else -> {

                }
            }
        }
    }


    companion object {
        private val ORDER = object :
            DiffUtil.ItemCallback<Order>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(
                oldConcert: Order,
                newConcert: Order
            ) = oldConcert.id == newConcert.id

            override fun areContentsTheSame(
                oldConcert: Order,
                newConcert: Order
            ) = oldConcert == newConcert
        }
    }
}

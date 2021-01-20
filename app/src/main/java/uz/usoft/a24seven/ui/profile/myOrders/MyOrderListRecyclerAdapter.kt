package uz.usoft.a24seven.ui.profile.myOrders

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_order.view.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.network.di.MockData

class MyOrderListRecyclerAdapter(val orderListType:String="") : RecyclerView.Adapter<MyOrderListRecyclerAdapter.ViewHolder>() {

    var productsList: List<MockData.ProductObject>? = MockData.getFeedbackList()

    fun updateList(productsList: List<MockData.ProductObject>) {
        this.productsList = productsList
        notifyDataSetChanged()
    }


    var onItemClick: ((MockData.ProductObject) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
    )

    override fun getItemCount() = productsList?.size ?: 0

    override fun onBindViewHolder(holder: MyOrderListRecyclerAdapter.ViewHolder, position: Int) {
        holder.bindData(productsList!![position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val orderStatus=itemView.orderStatus
        init {
            itemView.orderDetails.setOnClickListener {
                onItemClick?.invoke(productsList!![adapterPosition])
            }
        }

        fun bindData(product: MockData.ProductObject) {
            when(orderListType){
                "active"->{
                    orderStatus.setTextColor(Color.parseColor("#1BC06D"))
                    orderStatus.text="Активные"
                }
                "inactive"->{
                    orderStatus.setTextColor(Color.parseColor("#F8B068"))
                    orderStatus.text="Ожидание"
                }
                "delivered"->{
                    orderStatus.setTextColor(Color.parseColor("#DB3022"))
                    orderStatus.text="Доставлен"
                }
            }
        }
    }
}

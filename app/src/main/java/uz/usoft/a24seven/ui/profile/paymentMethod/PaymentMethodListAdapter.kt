package uz.usoft.a24seven.ui.profile.paymentMethod

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_payment_method.view.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.network.di.MockData

class PaymentMethodListAdapter (): RecyclerView.Adapter<PaymentMethodListAdapter.ViewHolder>() {
    var productsList: List<MockData.PaymentMethodObject>? = MockData.getPaymentMethodList()
    var selected=""
    fun updateList(productsList: List<MockData.PaymentMethodObject>) {
        this.productsList = productsList
        notifyDataSetChanged()
    }

    fun getDefaultPaymentMethod():String{
        return selected
    }

    var onItemClick: ((MockData.PaymentMethodObject) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_payment_method, parent, false)
    )

    override fun getItemCount() = productsList?.size ?: 0

    override fun onBindViewHolder(holder: PaymentMethodListAdapter.ViewHolder, position: Int) {
        holder.bindData(productsList!![position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val paymentMethod=itemView.paymentMethod
        val checkbx=itemView.checkBox

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(productsList!![adapterPosition])

                if(!productsList!![adapterPosition].isDefault)
                {
                    for (method in productsList!!)
                        method.isDefault=false

                    productsList!![adapterPosition].isDefault=true
                    selected=productsList!![adapterPosition].name
                }
                else{
                    productsList!![adapterPosition].isDefault=false
                }
                notifyDataSetChanged()
            }

                checkbx.setOnClickListener {
                    onItemClick?.invoke(productsList!![adapterPosition])

                    if(!productsList!![adapterPosition].isDefault)
                    {
                        for (method in productsList!!)
                            method.isDefault=false

                        productsList!![adapterPosition].isDefault=true
                    }
                    else{
                        productsList!![adapterPosition].isDefault=false
                    }
                    notifyDataSetChanged()
                }
        }

        fun bindData(product: MockData.PaymentMethodObject) {
            paymentMethod.text=product.name
            checkbx.isChecked=product.isDefault
        }
    }
}

package uz.usoft.a24seven.ui.profile.paymentMethod

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import uz.usoft.a24seven.databinding.ItemPaymentMethodBinding
import uz.usoft.a24seven.network.models.MockData

class PaymentMethodListAdapter() : RecyclerView.Adapter<PaymentMethodListAdapter.ViewHolder>() {
    var productsList: List<MockData.PaymentMethodObject>? = MockData.getPaymentMethodList()
    var selected = ""
    fun updateList(productsList: List<MockData.PaymentMethodObject>) {
        this.productsList = productsList
        notifyDataSetChanged()
    }

    fun getDefaultPaymentMethod(): String {
        return selected
    }

    var onItemClick: ((MockData.PaymentMethodObject) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPaymentMethodBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = productsList?.size ?: 0

    override fun onBindViewHolder(holder: PaymentMethodListAdapter.ViewHolder, position: Int) {
        holder.bindData(productsList!![position])
    }

    inner class ViewHolder(var binding: ItemPaymentMethodBinding) : RecyclerView.ViewHolder(binding.root) {


        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(productsList!![adapterPosition])

                if (!productsList!![adapterPosition].isDefault) {
                    for (method in productsList!!)
                        method.isDefault = false

                    productsList!![adapterPosition].isDefault = true
                    selected = productsList!![adapterPosition].name
                } else {
                    productsList!![adapterPosition].isDefault = false
                }
                notifyDataSetChanged()
            }

            binding.checkBox.setOnClickListener {
                onItemClick?.invoke(productsList!![adapterPosition])

                if (!productsList!![adapterPosition].isDefault) {
                    for (method in productsList!!)
                        method.isDefault = false

                    productsList!![adapterPosition].isDefault = true
                } else {
                    productsList!![adapterPosition].isDefault = false
                }
                notifyDataSetChanged()
            }
        }

        fun bindData(product: MockData.PaymentMethodObject) {
            val binding = binding as ItemPaymentMethodBinding

            binding.paymentMethod.text = product.name
            binding.checkBox.isChecked = product.isDefault
        }
    }
}

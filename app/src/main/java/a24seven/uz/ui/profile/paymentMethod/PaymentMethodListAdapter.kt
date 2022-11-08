package a24seven.uz.ui.profile.paymentMethod

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import a24seven.uz.data.PrefManager
import a24seven.uz.databinding.ItemPaymentMethodBinding
import a24seven.uz.network.models.MockData

class PaymentMethodListAdapter(val context:Context) : RecyclerView.Adapter<PaymentMethodListAdapter.ViewHolder>() {
    var paymentMethodList: List<MockData.PaymentMethodObject>? = MockData.getPaymentMethodList(context)
    var selected = ""
    fun updateList(productsList: List<MockData.PaymentMethodObject>) {
        this.paymentMethodList = productsList
        notifyDataSetChanged()
    }

    fun getDefaultPaymentMethod(): String {
        return selected
    }

    var onItemClick: ((MockData.PaymentMethodObject) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        selected= PrefManager.getPaymentMethod(context)
        val binding = ItemPaymentMethodBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = paymentMethodList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(paymentMethodList!![position])
    }

    inner class ViewHolder(var binding: ItemPaymentMethodBinding) : RecyclerView.ViewHolder(binding.root) {


        init {
            itemView.setOnClickListener {
                selected = if (paymentMethodList!![bindingAdapterPosition].name != selected) {
                    paymentMethodList!![bindingAdapterPosition].name
                } else {
                    ""
                }
                notifyDataSetChanged()
            }

            binding.checkBox.setOnClickListener {
                selected = if (paymentMethodList!![bindingAdapterPosition].name != selected) {
                    paymentMethodList!![bindingAdapterPosition].name
                } else {
                    ""
                }
                notifyDataSetChanged()
            }
        }

        fun bindData(paymentMethod: MockData.PaymentMethodObject) {

            binding.paymentMethod.text = paymentMethod.name
            binding.checkBox.isChecked =  paymentMethod.name == selected
        }
    }
}

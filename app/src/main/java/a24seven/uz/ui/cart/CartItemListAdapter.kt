package a24seven.uz.ui.cart

import a24seven.uz.R
import a24seven.uz.databinding.ItemCartItemBinding
import a24seven.uz.network.models.CartItem
import a24seven.uz.network.models.Product
import a24seven.uz.utils.image
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CartItemListAdapter(val context: Context) :
    RecyclerView.Adapter<CartItemListAdapter.ViewHolder>() {
    var productsList: List<Product>? = null

    fun updateList(productsList: List<Product>) {
        this.productsList = productsList
        notifyDataSetChanged()
    }

    fun updateItems(cartItems: List<CartItem>) {
        cartItems.forEach {
            for (product in this.productsList!!) {
                if (product.id == it.id)
                    product.count = it.count
            }
        }
        notifyDataSetChanged()
    }


    var onItemClick: ((Product) -> Unit)? = null
    var remove: ((Product) -> Unit)? = null
    var updateCart: ((Product, inc: Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

    }

    override fun getItemCount() = productsList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(productsList!![position])
    }

    inner class ViewHolder(var binding: ItemCartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(productsList!![bindingAdapterPosition])
            }

            binding.remove.setOnClickListener {
                remove?.invoke(productsList!![bindingAdapterPosition])
            }

            binding.inc.setOnClickListener {
                updateCart?.invoke(productsList!![bindingAdapterPosition], true)
            }
            binding.dec.setOnClickListener {
                updateCart?.invoke(productsList!![bindingAdapterPosition], false)
            }
        }

        fun bindData(product: Product) {
            binding.productName.text = product.name
            binding.productImage.image(context, product.image!!.path)
            binding.unitName.text = "${product.price} сум/${product.unit.name}"
            if (product.discount_percent > 0) {
                binding.productPrice.text = context.getString(
                    R.string.money_format_sum_unit,
                    product.price_discount
                )
            } else {
                binding.productPrice.text = context.getString(
                    R.string.money_format_sum_unit,
                    product.price * product.cart_count
                )
            }
            binding.count.text = "${(product.count * product.unit.count).toInt()}"
        }
    }
}

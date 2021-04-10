package uz.usoft.a24seven.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.ItemCartItemBinding
import uz.usoft.a24seven.network.models.CartItem
import uz.usoft.a24seven.network.models.MockData
import uz.usoft.a24seven.network.models.Product
import uz.usoft.a24seven.utils.image

class CartItemListAdapter(val context: Context) : RecyclerView.Adapter<CartItemListAdapter.ViewHolder>() {
    var productsList: List<Product>? = null

    fun updateList(productsList: List<Product>) {
        this.productsList = productsList
        notifyDataSetChanged()
    }

    fun updateItems(cartItems: List<CartItem>) {
        cartItems.forEach {
            for (product in this.productsList!!)
            {
                if(product.id==it.id)
                    product.count=it.count
            }
        }
        notifyDataSetChanged()
    }


    var onItemClick: ((Product) -> Unit)? = null
    var remove: ((Product) -> Unit)? = null
    var updateCart: ((Product,inc:Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

    }

    override fun getItemCount() = productsList?.size ?: 0

    override fun onBindViewHolder(holder: CartItemListAdapter.ViewHolder, position: Int) {
        holder.bindData(productsList!![position])
    }

    inner class ViewHolder(var binding: ItemCartItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(productsList!![bindingAdapterPosition])
            }

            binding.remove.setOnClickListener {
                remove?.invoke(productsList!![bindingAdapterPosition])
            }

            binding.inc.setOnClickListener {
                updateCart?.invoke(productsList!![bindingAdapterPosition],true)
            }
            binding.dec.setOnClickListener {
                updateCart?.invoke(productsList!![bindingAdapterPosition],false)
            }
        }

        fun bindData(product: Product) {
            binding.productName.text= product.name
            binding.productImage.image(context,product.image!!.path)
            if(product.discount_percent>0)
            {
                binding.productPrice.text = context.getString(R.string.money_format_sum_unit, product.price_discount,product.unit.name)
            }
            else{
                binding.productPrice.text = context.getString(R.string.money_format_sum_unit, product.price,product.unit.name)
            }
            binding.count.text=context.getString(R.string.count_with_unit, product.count,product.unit.name)

        }
    }
}

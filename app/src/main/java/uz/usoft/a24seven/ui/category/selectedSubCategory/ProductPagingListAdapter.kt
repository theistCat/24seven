package uz.usoft.a24seven.ui.category.selectedSubCategory

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import uz.usoft.a24seven.R
import uz.usoft.a24seven.data.PrefManager
import uz.usoft.a24seven.databinding.ItemProductGridBinding
import uz.usoft.a24seven.network.models.Product
import uz.usoft.a24seven.utils.image

class ProductPagingListAdapter (val context : Context): PagingDataAdapter<Product,ProductPagingListAdapter.ViewHolder>(PRODUCT)
{


    fun update(position:Int,updateValue:Boolean)
    {
        getItem(position)?.is_favorite=updateValue
        notifyItemChanged(position)
    }

    var onItemClick: ((Product) -> Unit)? = null
    var onFavClick: ((Product,position:Int) -> Unit)? = null
    var addToCart: ((Product) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return ViewHolder(binding)

    }


    override fun onBindViewHolder(holder: ProductPagingListAdapter.ViewHolder, position: Int) {
        holder.bindData(getItem(position) as Product)
    }

    inner class ViewHolder(var binding: ItemProductGridBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(bindingAdapterPosition) as Product)
            }
            binding.productIsFav.setOnClickListener {
                onFavClick?.invoke(getItem(bindingAdapterPosition) as Product,bindingAdapterPosition)
            }
            binding.addToCart.setOnClickListener {
                addToCart?.invoke(getItem(bindingAdapterPosition) as Product)
            }

        }

        fun bindData(product: Product) {
            when (binding) {
                else -> {
                    val binding = binding as ItemProductGridBinding
                    if (product.discount_percent > 0) {
                        binding.productPrice.text = context.getString(
                            R.string.money_format_sum_unit,
                            product.price_discount,
                            product.unit.name
                        )
                        binding.productOldPrice.text =
                            context.getString(R.string.money_format_sum, product.price)
                        binding.productTag.text =
                            context.getString(R.string.discount, product.discount_percent)
                    } else {
                        binding.productPrice.text = context.getString(
                            R.string.money_format_sum_unit,
                            product.price,
                            product.unit.name
                        )
                        binding.productOldPrice.visibility = View.INVISIBLE
                        binding.productTag.isVisible = false
                    }
                    binding.productCategory.text = product.category.name
                    binding.productComments.text =
                        context.getString(R.string.comments_count, product.comments_count)
                    binding.productIsFav.isChecked = product.is_favorite
                    binding.productName.text = product.name
                    binding.productImage.image(context, product.image!!.path_thumb)

                    if (product.is_cart) {
                        binding.addToCart.isEnabled = false
                        binding.addToCart.icon =
                            ContextCompat.getDrawable(context, R.drawable.ic_check)
                    } else {
                        binding.addToCart.isEnabled = true
                        binding.addToCart.icon =
                            ContextCompat.getDrawable(context, R.drawable.ic_add_cart)
                    }

                    binding.addToCart.isVisible=product.product_count?:0!=0
                }
            }
        }
    }
    companion object {
        private val PRODUCT = object :
            DiffUtil.ItemCallback<Product>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(oldProduct: Product,
                                         newProduct: Product) = oldProduct.id == newProduct.id

            override fun areContentsTheSame(oldProduct: Product,
                                            newProduct: Product) = oldProduct == newProduct
        }
    }
}
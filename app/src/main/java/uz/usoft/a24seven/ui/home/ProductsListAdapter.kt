package uz.usoft.a24seven.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.ItemProductBinding
import uz.usoft.a24seven.databinding.ItemProductGridBinding
import uz.usoft.a24seven.network.models.Product
import uz.usoft.a24seven.utils.image

class ProductsListAdapter( val context: Context,val isGrid: Boolean = false) :
    RecyclerView.Adapter<ProductsListAdapter.ViewHolder>() {

    var productsList: List<Product>? = null

    fun updateList(productsList: List<Product>) {
        this.productsList = productsList
        notifyDataSetChanged()
    }


    var onItemClick: ((Product) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            if (isGrid) ItemProductGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            else
                ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

    }

    override fun getItemCount() = productsList?.size ?: 0

    override fun onBindViewHolder(holder: ProductsListAdapter.ViewHolder, position: Int) {
        holder.bindData(productsList!![position])
    }

    inner class ViewHolder(var binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(productsList!![adapterPosition])
            }
        }

        fun bindData(product: Product) {
            when (binding) {
                is ItemProductGridBinding -> {
                    val binding = binding as ItemProductGridBinding
                    binding.productPrice.text = context.getString(R.string.money_format_sum_unit,product.price,product.unit.name)
                    binding.productName.text=product.name
                    binding.productComments.text=context.getString(R.string.comments_count,product.comments_count)
                    binding.productIsFav.isChecked=product.is_favorite
                    binding.productImage.image(context,product.image.path_thumb)
                }
                is ItemProductBinding -> {
                    val binding = binding as ItemProductBinding
                    if(product.discount_percent>0)
                    {
                        binding.productPrice.text = context.getString(R.string.money_format_sum_unit, product.price_discount,product.unit.name)
                        binding.productOldPrice.text = context.getString(R.string.money_format_sum, product.price_discount)
                        binding.productTag.text=context.getString(R.string.discount,product.discount_percent)
                    }
                    else{
                        binding.productPrice.text = context.getString(R.string.money_format_sum_unit, product.price,product.unit.name)
                        binding.productOldPrice.isVisible=false
                        binding.productTag.isVisible=false
                    }
                    binding.productCategory.text=product.category.name
                    binding.productComments.text=context.getString(R.string.comments_count,product.comments_count)
                    binding.productIsFav.isChecked=product.is_favorite
                    binding.productName.text=product.name
                    binding.productImage.image(context,product.image.path_thumb)
                }

            }
        }
    }

}

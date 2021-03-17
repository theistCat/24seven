package uz.usoft.a24seven.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import uz.usoft.a24seven.databinding.ItemProductBinding
import uz.usoft.a24seven.databinding.ItemProductGridBinding
import uz.usoft.a24seven.network.models.MockData
import uz.usoft.a24seven.network.models.MockData.ProductObject

class ProductsListAdapter(val isGrid: Boolean = false, val isPopular: Boolean = false) :
    RecyclerView.Adapter<ProductsListAdapter.ViewHolder>() {

    var productsList: List<ProductObject>? =
        if (isPopular) MockData.getPopularProductList() else MockData.getProductList()

    fun updateList(productsList: List<ProductObject>) {
        this.productsList = productsList
        notifyDataSetChanged()
    }


    var onItemClick: ((ProductObject) -> Unit)? = null

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

        fun bindData(product: ProductObject) {
            when (binding) {
                is ItemProductGridBinding -> {
                    val binding = binding as ItemProductGridBinding
                    binding.productPrice.text = product.price

                }
                is ItemProductBinding -> {
                    val binding = binding as ItemProductBinding
                    binding.productPrice.text = product.price
                }

            }
        }
    }
}

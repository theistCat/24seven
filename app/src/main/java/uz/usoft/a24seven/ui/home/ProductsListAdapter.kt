package uz.usoft.a24seven.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import uz.usoft.a24seven.R
import uz.usoft.a24seven.data.PrefManager
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


    fun getItem(position:Int):Product?
    {
         productsList!!.forEach { product ->
            if (product.id == position)
                return product
        }
        return null
    }

    fun update(updateId:Int,updateValue:Boolean)
    {
        Log.d("favTag", " $updateId to $updateValue")
        //productsList!![updateId].is_favorite=updateValue
        this.productsList?.forEach {
            if (it.id==updateId) {
                it.is_favorite = updateValue
                Log.d("favTag","changed")
                notifyDataSetChanged()
            }
        }
    }

    var onItemClick: ((Product) -> Unit)? = null
    var addToCart: ((Product) -> Unit)? = null

    var onFavClick: ((Product,position:Int) -> Unit)? = null
    var addFav: ((Product) -> Unit)? = null
    var removeFav: ((Product) -> Unit)? = null

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
                onItemClick?.invoke(productsList!![bindingAdapterPosition])
            }


            when (binding) {
                is ItemProductGridBinding -> {
                    val binding = binding as ItemProductGridBinding
                    binding.productIsFav.setOnClickListener {
                        onFavClick?.invoke(
                            productsList!![bindingAdapterPosition] as Product,
                            bindingAdapterPosition
                        )

                        Log.d("favTag", "change ${productsList!![bindingAdapterPosition].id} at position $bindingAdapterPosition")
                    }
                    binding.addToCart.setOnClickListener {
                        addToCart?.invoke(productsList!![bindingAdapterPosition] as Product)
                    }
                }
                is ItemProductBinding -> {
                    val binding = binding as ItemProductBinding
                    binding.productIsFav.setOnClickListener {
                        if(binding.productIsFav.isChecked)
                            addFav?.invoke(productsList!![bindingAdapterPosition])
                        else
                            removeFav?.invoke(productsList!![bindingAdapterPosition])
                    }
                    binding.addToCart.setOnClickListener {
                        addToCart?.invoke(productsList!![bindingAdapterPosition])
                    }
                }
            }
        }

        fun bindData(product: Product) {
            when (binding) {
                is ItemProductGridBinding -> {
                    val binding = binding as ItemProductGridBinding
                    if(product.discount_percent>0)
                    {
                        binding.productPrice.text = context.getString(R.string.money_format_sum_unit, product.price_discount,product.unit.name)
                        binding.productOldPrice.text = context.getString(R.string.money_format_sum, product.price_discount)
                        binding.productTag.text=context.getString(R.string.discount,product.discount_percent)
                    }
                    else{
                        binding.productPrice.text = context.getString(R.string.money_format_sum_unit, product.price,product.unit.name)
                        binding.productOldPrice.visibility= View.INVISIBLE
                        binding.productTag.isVisible=false
                    }
                    binding.productCategory.text=product.category.name
                    binding.productComments.text=context.getString(R.string.comments_count,product.comments_count)
                    binding.productIsFav.isChecked=product.is_favorite
                    binding.productName.text=product.name
                    binding.productImage.image(context,product.image!!.path_thumb)

                    if (PrefManager.getInstance(context).getBoolean(product.id.toString(),false))
                    {
                        binding.addToCart.isEnabled=false
                        binding.addToCart.icon= ContextCompat.getDrawable(context,R.drawable.ic_check)
                    }
                    else
                    {
                        binding.addToCart.isEnabled=true
                        binding.addToCart.icon= ContextCompat.getDrawable(context,R.drawable.ic_add_cart)
                    }


                    binding.addToCart.isVisible=product.product_count!=0
                }
                is ItemProductBinding -> {
                    val binding = binding as ItemProductBinding
                    if(product.discount_percent>0)
                    {
                        binding.productPrice.text = context.getString(R.string.money_format_sum_unit, product.price_discount,product.unit.name)
                        binding.productOldPrice.text = context.getString(R.string.money_format_sum, product.price)
                        binding.productTag.text=context.getString(R.string.discount,product.discount_percent)
                    }
                    else{
                        binding.productPrice.text = context.getString(R.string.money_format_sum_unit, product.price,product.unit.name)
                        binding.productOldPrice.visibility= View.INVISIBLE
                        binding.productTag.isVisible=false
                    }
                    binding.productCategory.text=product.category.name
                    binding.productComments.text=context.getString(R.string.comments_count,product.comments_count)
                    binding.productIsFav.isChecked=product.is_favorite
                    binding.productName.text=product.name
                    binding.productImage.image(context,product.image!!.path_thumb)


                    if (PrefManager.getInstance(context).getBoolean(product.id.toString(),false))
                    {
                        binding.addToCart.isEnabled=false
                        binding.addToCart.icon= ContextCompat.getDrawable(context,R.drawable.ic_check)
                    }
                    else
                    {
                        binding.addToCart.isEnabled=true
                        binding.addToCart.icon= ContextCompat.getDrawable(context,R.drawable.ic_add_cart)
                    }

                    binding.addToCart.isVisible=product.product_count?:0!=0
                }

            }
        }
    }

}

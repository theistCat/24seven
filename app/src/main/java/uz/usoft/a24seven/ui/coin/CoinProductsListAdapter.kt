package uz.usoft.a24seven.ui.coin

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
import uz.usoft.a24seven.databinding.ItemCoinProductBinding
import uz.usoft.a24seven.databinding.ItemCoinProductGridBinding
import uz.usoft.a24seven.databinding.ItemProductBinding
import uz.usoft.a24seven.databinding.ItemProductGridBinding
import uz.usoft.a24seven.network.models.CoinProduct
import uz.usoft.a24seven.network.models.Product
import uz.usoft.a24seven.utils.image

class CoinProductsListAdapter(val context: Context, val isGrid: Boolean = false) :
    RecyclerView.Adapter<CoinProductsListAdapter.ViewHolder>() {

    var productsList: List<CoinProduct>? = null

    fun updateList(productsList: List<CoinProduct>) {
        this.productsList = productsList
        notifyDataSetChanged()
    }


    fun update(updateId:Int,updateValue:Boolean)
    {
        Log.d("favTag", " $updateId to $updateValue")
        //productsList!![updateId].is_favorite=updateValue
        this.productsList?.forEach {
            if (it.id==updateId) {
               // it.is_favorite = updateValue
                Log.d("favTag","changed")
                notifyDataSetChanged()
            }
        }
    }

    var onItemClick: ((CoinProduct) -> Unit)? = null
    var addToCart: ((CoinProduct) -> Unit)? = null

    var onFavClick: ((CoinProduct,position:Int) -> Unit)? = null
    var addFav: ((CoinProduct) -> Unit)? = null
    var removeFav: ((CoinProduct) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            if (isGrid) ItemCoinProductGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            else
                ItemCoinProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

    }

    override fun getItemCount() = productsList?.size ?: 0

    override fun onBindViewHolder(holder: CoinProductsListAdapter.ViewHolder, position: Int) {
        holder.bindData(productsList!![position])
    }

    inner class ViewHolder(var binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

            itemView.setOnClickListener {
                onItemClick?.invoke(productsList!![bindingAdapterPosition])
            }


            when (binding) {
                is ItemCoinProductGridBinding -> {
                    val binding = binding as ItemCoinProductGridBinding
                    binding.productIsFav.setOnClickListener {
                        onFavClick?.invoke(
                            productsList!![bindingAdapterPosition] as CoinProduct,
                            bindingAdapterPosition
                        )

                        Log.d("favTag", "change ${productsList!![bindingAdapterPosition].id} at position $bindingAdapterPosition")
                    }
                    binding.addToCart.setOnClickListener {
                        addToCart?.invoke(productsList!![bindingAdapterPosition] as CoinProduct)
                    }
                }
                is ItemCoinProductBinding -> {
                    val binding = binding as ItemCoinProductBinding
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

        fun bindData(product: CoinProduct) {
            when (binding) {
                is ItemCoinProductGridBinding -> {
                    val binding = binding as ItemCoinProductGridBinding

                        binding.productPrice.text = context.getString(R.string.money_format_coin_unit, product.price)
                        binding.productOldPrice.visibility= View.GONE
                        binding.productTag.isVisible=false

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
                }
                is ItemCoinProductBinding -> {
                    val binding = binding as ItemCoinProductBinding

                        binding.productPrice.text = context.getString(R.string.money_format_coin_unit, product.price)
                        binding.productOldPrice.visibility= View.GONE
                        binding.productTag.isVisible=false

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
                }

            }
        }
    }

}

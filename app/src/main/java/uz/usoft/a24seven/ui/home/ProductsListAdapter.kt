package uz.usoft.a24seven.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_product.view.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.network.di.MockData
import uz.usoft.a24seven.network.di.MockData.ProductObject

class ProductsListAdapter(val isGrid:Boolean=false,val isPopular:Boolean=false) : RecyclerView.Adapter<ProductsListAdapter.ViewHolder>() {

    var productsList: List<ProductObject>? =if(isPopular) MockData.getPopularProductList() else MockData.getProductList()

    fun updateList(productsList: List<ProductObject>) {
        this.productsList = productsList
        notifyDataSetChanged()
    }


    var onItemClick: ((ProductObject) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        if(isGrid)LayoutInflater.from(parent.context).inflate(R.layout.item_product_grid, parent, false)
        else LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
    )

    override fun getItemCount() = productsList?.size ?: 0

    override fun onBindViewHolder(holder: ProductsListAdapter.ViewHolder, position: Int) {
        holder.bindData(productsList!![position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productPrice=itemView.productPrice

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(productsList!![adapterPosition])
            }
        }

        fun bindData(product: ProductObject) {
            productPrice.text=product.price
        }
    }
}

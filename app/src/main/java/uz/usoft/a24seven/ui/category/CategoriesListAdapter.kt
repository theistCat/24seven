package uz.usoft.a24seven.ui.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.usoft.a24seven.R
import uz.usoft.a24seven.network.di.MockData

class CategoriesListAdapter : RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {
    var productsList: List<MockData.ProductObject>? = MockData.getProductList()

    fun updateList(productsList: List<MockData.ProductObject>) {
        this.productsList = productsList
        notifyDataSetChanged()
    }


    var onItemClick: ((MockData.ProductObject) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
         LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
    )

    override fun getItemCount() = productsList?.size ?: 0

    override fun onBindViewHolder(holder: CategoriesListAdapter.ViewHolder, position: Int) {
        holder.bindData(productsList!![position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(productsList!![adapterPosition])
            }
        }

        fun bindData(product: MockData.ProductObject) {
        }
    }
}

package uz.usoft.a24seven.ui.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.android.synthetic.main.item_news.view.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.ItemNewsBinding
import uz.usoft.a24seven.databinding.ItemNewsGridBinding
import uz.usoft.a24seven.network.di.MockData
import uz.usoft.a24seven.network.di.MockData.ProductObject

class NewsListAdapter(val isGrid: Boolean = false) :
    RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {
    var productsList: List<ProductObject>? = MockData.getProductList()

    fun updateList(productsList: List<ProductObject>) {
        this.productsList = productsList
        notifyDataSetChanged()
    }


    var onItemClick: ((ProductObject) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            if (isGrid) ItemNewsGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            else ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount() = productsList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(productsList!![position])
    }

    inner class ViewHolder(var binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val parent = itemView.parentLay

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(productsList!![adapterPosition])
            }
        }

        fun bindData(product: ProductObject) {
        }
    }
}

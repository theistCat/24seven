package uz.usoft.a24seven.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import uz.usoft.a24seven.databinding.ItemCategoryBinding
import uz.usoft.a24seven.network.models.CategoryObject
import uz.usoft.a24seven.network.models.MockData

class CategoriesListAdapter : RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {
    var productsList: List<CategoryObject>? = emptyList()

    fun updateList(productsList: List<CategoryObject>) {
        this.productsList = productsList
        notifyDataSetChanged()
    }


    var onItemClick: ((CategoryObject) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

    }

    override fun getItemCount() = productsList?.size ?: 0

    override fun onBindViewHolder(holder: CategoriesListAdapter.ViewHolder, position: Int) {
        holder.bindData(productsList!![position])
    }

    inner class ViewHolder(var binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(productsList!![adapterPosition])
            }
        }

        fun bindData(product: CategoryObject) {
            val binding = binding as ItemCategoryBinding
            binding.categoryName.text = product.name
        }
    }
}

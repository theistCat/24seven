package a24seven.uz.ui.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import a24seven.uz.databinding.ItemCategoryBinding
import a24seven.uz.network.models.CategoryObject
import a24seven.uz.utils.image

class CategoriesListAdapter(val context: Context) : RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {
    var productsList: List<CategoryObject>? = null

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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
            binding.categoryImage.image(context,product.image?:"")
        }
    }
}

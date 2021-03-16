package uz.usoft.a24seven.ui.category.subCategory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.android.synthetic.main.item_sub_category.view.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentSubCategoriesBinding
import uz.usoft.a24seven.databinding.ItemSubCategoryBinding
import uz.usoft.a24seven.network.di.MockData

class SubCategoriesListAdapter : RecyclerView.Adapter<SubCategoriesListAdapter.ViewHolder>() {
    var productsList: List<MockData.ProductObject>? = MockData.getSubCategoriesList()


    fun updateList(productsList: List<MockData.ProductObject>) {
        this.productsList = productsList
        notifyDataSetChanged()
    }


    var onItemClick: ((MockData.ProductObject) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSubCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = productsList?.size ?: 0

    override fun onBindViewHolder(holder: SubCategoriesListAdapter.ViewHolder, position: Int) {
        holder.bindData(productsList!![position])
    }

    inner class ViewHolder(var binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {


        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(productsList!![adapterPosition])
            }
        }

        fun bindData(product: MockData.ProductObject) {
            val binding = binding as ItemSubCategoryBinding
            binding.subCategoryName.text = product.name
        }
    }
}
package uz.usoft.a24seven.ui.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import uz.usoft.a24seven.databinding.ItemFilterOptionBinding
import uz.usoft.a24seven.network.di.MockData

class   FilterOptionsAdapter() : RecyclerView.Adapter<FilterOptionsAdapter.ViewHolder>() {

    var productsList: List<MockData.ProductObject>? = MockData.getFeedbackList()

    fun updateList(productsList: List<MockData.ProductObject>) {
        this.productsList = productsList
        notifyDataSetChanged()
    }


    var onItemClick: ((MockData.ProductObject) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemFilterOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = productsList?.size ?: 0

    override fun onBindViewHolder(holder: FilterOptionsAdapter.ViewHolder, position: Int) {
        holder.bindData(productsList!![position])
    }

    inner class ViewHolder(var binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(productsList!![adapterPosition])
            }
        }

        fun bindData(product: MockData.ProductObject) {
        }
    }
}

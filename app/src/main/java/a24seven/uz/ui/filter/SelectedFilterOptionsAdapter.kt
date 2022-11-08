package a24seven.uz.ui.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import a24seven.uz.databinding.ItemFilterSelectedOptionBinding

class   SelectedFilterOptionsAdapter() : RecyclerView.Adapter<SelectedFilterOptionsAdapter.ViewHolder>() {

    var characteristics: List<String>? = null
    var filter: ArrayList<String>?=null

    fun updateList(characteristics: List<String>) {
        this.characteristics = characteristics
        notifyDataSetChanged()
    }


    var onItemClick: ((add:Boolean,String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemFilterSelectedOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = characteristics?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(characteristics!![position])
    }

    inner class ViewHolder(var binding: ItemFilterSelectedOptionBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                binding.checkBox.isChecked=!binding.checkBox.isChecked
                if(binding.checkBox.isChecked)
                    onItemClick?.invoke(true,characteristics!![bindingAdapterPosition])
                else
                    onItemClick?.invoke(false,characteristics!![bindingAdapterPosition])
            }

            binding.checkBox.setOnClickListener {
                if(binding.checkBox.isChecked)
                    onItemClick?.invoke(true,characteristics!![bindingAdapterPosition])
                else
                    onItemClick?.invoke(false,characteristics!![bindingAdapterPosition])
            }
        }

        fun bindData(characteristics: String) {
            binding.checkBox.text=characteristics
            binding.checkBox.isChecked=filter!!.contains(characteristics)
        }
    }
}

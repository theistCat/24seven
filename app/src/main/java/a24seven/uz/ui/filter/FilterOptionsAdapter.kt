package a24seven.uz.ui.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import a24seven.uz.R
import a24seven.uz.databinding.ItemFilterOptionBinding
import a24seven.uz.network.models.Characteristics

class   FilterOptionsAdapter(val context: Context) : RecyclerView.Adapter<FilterOptionsAdapter.ViewHolder>() {

    var characteristics: List<Characteristics>? = null

    var filter: HashMap<String,ArrayList<String>>?=null

    fun updateList(characteristics: List<Characteristics>) {
        this.characteristics = characteristics
        notifyDataSetChanged()
    }


    var onItemClick: ((Characteristics) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemFilterOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = characteristics?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(characteristics!![position])
    }

    inner class ViewHolder(var binding: ItemFilterOptionBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(characteristics!![bindingAdapterPosition])
            }
        }

        fun bindData(characteristics: Characteristics) {
            binding.filterOptionTitle.text=characteristics.name
            if(filter!=null)
                if(filter!![characteristics.id.toString()]?.isNotEmpty() == true)
                    binding.filterOptionValue.text= filter!![characteristics.id.toString()]!!.joinToString()
                else
                {
                    binding.filterOptionValue.text=context.getString(R.string.any)
                }
        }
    }
}

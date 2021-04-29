package uz.usoft.a24seven.ui.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import uz.usoft.a24seven.databinding.ItemCharacteristicsBinding
import uz.usoft.a24seven.databinding.ItemFeedbackBinding
import uz.usoft.a24seven.network.models.Characteristics
import uz.usoft.a24seven.network.models.Comment
import uz.usoft.a24seven.network.models.MockData
import uz.usoft.a24seven.network.models.Product
import uz.usoft.a24seven.ui.home.ProductsListAdapter

class CharacteristicsListAdapter() : RecyclerView.Adapter<CharacteristicsListAdapter.ViewHolder>()  {


    var characteristicsList: List<Characteristics>? = null
    var onItemClick: ((Any) -> Unit)? = null



    override fun getItemCount() = characteristicsList?.size ?: 0

    fun updateList(characteristicsList: List<Characteristics>) {
        this.characteristicsList = characteristicsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCharacteristicsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacteristicsListAdapter.ViewHolder, position: Int) {
        holder.bindData(characteristicsList!![position])
    }

    inner class ViewHolder(var binding: ItemCharacteristicsBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
//            itemView.setOnClickListener {
//                onItemClick?.invoke(characteristicsList!![bindingAdapterPosition])
//            }
        }

        fun bindData(characteristic: Characteristics) {
            binding.characteristicsName.text=characteristic.name
            binding.characteristicsValue.text= if(characteristic.value=="null") "" else characteristic.value

        }
    }
}

package a24seven.uz.ui.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import a24seven.uz.databinding.ItemCharacteristicsBinding
import a24seven.uz.network.models.Characteristics

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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

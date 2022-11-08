package a24seven.uz.ui.profile.myAddresses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import a24seven.uz.databinding.ItemAddressBinding
import a24seven.uz.network.models.Address

class AddressListAdapter : PagingDataAdapter<Address, AddressListAdapter.ViewHolder>(ADDRESS) {

    var onItemClick: ((Address) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(getItem(position) as Address)
    }

    inner class ViewHolder(var binding: ItemAddressBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(bindingAdapterPosition) as Address)
            }
        }

        fun bindData(address: Address) {
            binding.addressName.text=address.name
            binding.address.text=address.address
        }
    }

    companion object {
        private val ADDRESS = object :
            DiffUtil.ItemCallback<Address>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(oldConcert: Address,
                                         newConcert: Address
            ) = oldConcert.id == newConcert.id

            override fun areContentsTheSame(oldConcert: Address,
                                            newConcert: Address
            ) = oldConcert == newConcert
        }
    }
}

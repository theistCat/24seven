package uz.usoft.a24seven.ui.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import uz.usoft.a24seven.databinding.ItemFeedbackBinding
import uz.usoft.a24seven.network.models.Comment
import uz.usoft.a24seven.network.models.MockData
import uz.usoft.a24seven.network.models.Product

class FeedbackListAdapter() : PagingDataAdapter<Comment,FeedbackListAdapter.ViewHolder>(COMMENT) {

   // var productsList: List<MockData.ProductObject>? = MockData.getFeedbackList()

//    fun updateList(productsList: List<MockData.ProductObject>) {
//        this.productsList = productsList
//        notifyDataSetChanged()
//    }


    var onItemClick: ((Comment) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFeedbackBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedbackListAdapter.ViewHolder, position: Int) {
        holder.bindData(getItem(position) as Comment)
    }

    inner class ViewHolder(var binding: ItemFeedbackBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(bindingAdapterPosition) as Comment)
            }
        }

        fun bindData(comment: Comment) {
            binding.userName.text=comment.first_name
            binding.feedbackDate.text=comment.created_at
            binding.feedbackBody.text=comment.body

        }
    }

    companion object {
        private val COMMENT = object :
            DiffUtil.ItemCallback<Comment>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(oldConcert: Comment,
                                         newConcert: Comment
            ) = oldConcert.id == newConcert.id

            override fun areContentsTheSame(oldConcert: Comment,
                                            newConcert: Comment
            ) = oldConcert == newConcert
        }
    }
}

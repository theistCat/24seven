package a24seven.uz.ui.news

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import a24seven.uz.R
import a24seven.uz.databinding.ItemNewsGridBinding
import a24seven.uz.network.models.Post
import a24seven.uz.utils.image

class NewsPagingListAdapter(val context: Context) : PagingDataAdapter<Post, NewsPagingListAdapter.ViewHolder>(
    POST
) {
    var list: List<Post>? = null

    fun updateList(productsList: List<Post>) {
        this.list = productsList
        notifyDataSetChanged()
    }


    var onItemClick: ((Post) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(getItem(position) as Post)
    }

    inner class ViewHolder(var binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {


        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(bindingAdapterPosition) as Post)
            }
        }

        fun bindData(news: Post) {
                val binding = binding as ItemNewsGridBinding
                binding.newsImage.image(context,news.image, R.drawable.placeholder_news)
                binding.newsTitle.text=news.name
                binding.newsDate.text=news.created_at

        }
    }

    companion object {
        private val POST = object :
            DiffUtil.ItemCallback<Post>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(oldItem: Post,
                                         newItem: Post
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Post,
                                            newItem: Post
            ) = oldItem == newItem
        }
    }
}

package uz.usoft.a24seven.ui.news

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import uz.usoft.a24seven.databinding.ItemNewsBinding
import uz.usoft.a24seven.databinding.ItemNewsGridBinding
import uz.usoft.a24seven.network.models.Post
import uz.usoft.a24seven.utils.image

class NewsListAdapter(val context: Context, val isGrid: Boolean = false) :
    RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {
    var list: List<Post>? = null

    fun updateList(productsList: List<Post>) {
        this.list = productsList
        notifyDataSetChanged()
    }


    var onItemClick: ((Post) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            if (isGrid) ItemNewsGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            else ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount() = list?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(list!![position])
    }

    inner class ViewHolder(var binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {


        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(list!![adapterPosition])
            }
        }

        fun bindData(news: Post) {
            if(isGrid) {
                val binding = binding as ItemNewsGridBinding
                binding.newsImage.image(context,news.image)
                binding.newsTitle.text=news.name
                binding.newsDate.text=news.created_at
            }
            else {
                val binding = binding as ItemNewsBinding
                binding.newsImage.image(context,news.image)
                binding.newsTitle.text=news.name
                binding.newsDate.text=news.created_at
            }


        }
    }
}

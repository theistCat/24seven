package uz.usoft.a24seven.ui.products

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.ProductImageBinding

class ImagesAdapter(val context: Context): RecyclerView.Adapter<ImagesAdapter.ViewHolder>(){

    var images: List<String>?= null
    var imageUri: Uri= Uri.EMPTY

    fun updateList(images: List<String>) {
        this.images = images
        notifyDataSetChanged()
    }

    fun getimage(): String? {
       return images?.get(0)
    }

    //var onItemClick: ((MockData.Color) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesAdapter.ViewHolder {
        val binding = ProductImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= ViewHolder(
//        LayoutInflater.from(parent.context).inflate(R.layout.product_image, parent, false)
//    )

    override fun getItemCount()=images?.size?:0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(images!![position])

    }

    inner class ViewHolder(var binding: ProductImageBinding) : RecyclerView.ViewHolder(binding.root) {

        val image=binding.imageView

        init {
            itemView.setOnClickListener {
                        StfalconImageViewer.Builder<String>(context,images,::loadPosterImage).withBackgroundColor(
            ContextCompat.getColor(context,R.color.background)).show()
            }
        }

        fun bindData(images: String)
        {
            Glide.with(context).load(images).override(
                1000,
                1000
            ).placeholder(R.drawable.ic_logo_small).into(image)
//            if (imageUri== Uri.EMPTY)
//            {
//                Glide.with(context)
//                    .asBitmap()
//                    .load(images)
//                    .into(object : CustomTarget<Bitmap>(1000,1000){
//                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                            image.setImageBitmap(resource)
//                        }
//                        override fun onLoadCleared(placeholder: Drawable?) {
//                            // this is called when imageView is cleared on lifecycle call or for
//                            // some other reason.
//                            // if you are referencing the bitmap somewhere else too other than this imageView
//                            // clear it here as you can no longer have the bitmap
//                        }
//                    })
//                imageUri=Uri.parse(
//                    "android.resource://" + context.packageName
//                        .toString() + "/drawable/" + "ic_launcher"
//                )
//            }
        }

        private fun loadPosterImage(imageView: ImageView, image: String?) {
            Glide.with(imageView)
                .load(image)
                .placeholder(R.drawable.ic_logo_small)
                .into(imageView)
        }

    }
}
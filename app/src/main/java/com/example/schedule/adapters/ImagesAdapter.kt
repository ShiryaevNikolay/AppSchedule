package com.example.schedule.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.gallerypicker.model.GalleryData
import com.example.gallerypicker.utils.DateUtil
import com.example.gallerypicker.utils.RunOnUiThread
import com.example.schedule.R
import kotlinx.android.synthetic.main.item_images.view.*
import org.jetbrains.anko.doAsync

class ImagesAdapter(
    private var listImages: ArrayList<GalleryData>
) : RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>() {
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        mContext = parent.context
        return ImagesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_images, parent, false))
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        doAsync {
            RunOnUiThread(mContext).safely {
                try {
                    val requestListener: RequestListener<Drawable> = object :
                        RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            holder.itemView.image.alpha = 0.3f
                            holder.itemView.image.isEnabled = false
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: com.bumptech.glide.load.DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                    }
                    Glide.with(mContext).load(listImages[holder.adapterPosition].photoUri).apply(
                        RequestOptions().centerCrop().override(100)).transition(
                        DrawableTransitionOptions.withCrossFade()).listener(requestListener).into(holder.itemView.image)
                } catch (e: Exception) {
                }
            }
        }

        if (listImages[holder.adapterPosition].isEnabled) {
            holder.itemView.frame.alpha = 1.0f
            holder.itemView.image.isEnabled = true
        } else {
            holder.itemView.frame.alpha = 0.3f
            holder.itemView.image.isEnabled = false
        }

        if (listImages[holder.adapterPosition].mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
            holder.itemView.durationFrame.visibility = View.VISIBLE
            holder.itemView.durationLabel.text = DateUtil().millisToTime(listImages[holder.adapterPosition].duration.toLong())
        } else holder.itemView.durationFrame.visibility = View.GONE

        holder.itemView.deleteBtn.setOnClickListener {
            listImages.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return listImages.size
    }

    fun setList(listImages: ArrayList<GalleryData>) {
        this.listImages = listImages
        notifyDataSetChanged()
    }

    fun getList() : ArrayList<GalleryData> {
        return listImages
    }

    class ImagesViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
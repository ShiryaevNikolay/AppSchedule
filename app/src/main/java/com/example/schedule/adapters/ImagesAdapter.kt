package com.example.schedule.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.gallerypicker.model.GalleryData
import com.example.gallerypicker.utils.RunOnUiThread
import com.example.schedule.R
import com.example.schedule.interfaces.OnClickItemAdapterListener
import kotlinx.android.synthetic.main.item_images.view.*
import org.jetbrains.anko.doAsync
import java.io.File

class ImagesAdapter(
    private var mContext: Context
) : RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>() {
    private var arrayPathImage: ArrayList<String> = ArrayList()
    private var arrayPathImageForDelete: ArrayList<String> = ArrayList()
    private val clickItemListener: OnClickItemAdapterListener = mContext as OnClickItemAdapterListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        mContext = parent.context
        return ImagesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_images,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        doAsync {
            RunOnUiThread(mContext).safely {
                try {
                    val requestListener: RequestListener<Drawable> = object :
                        RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
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
                    Glide.with(mContext).load(arrayPathImage[holder.adapterPosition]).apply(
                        RequestOptions().centerCrop().override(100)
                    ).transition(
                        DrawableTransitionOptions.withCrossFade()
                    ).listener(requestListener).into(holder.itemView.image)
                } catch (e: Exception) {
                }
            }
        }

        holder.itemView.deleteBtn.setOnClickListener {
            if (File("${mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}", File(arrayPathImage[holder.adapterPosition]).name).exists()) {
                arrayPathImageForDelete.add("${mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}/${File(arrayPathImage[holder.adapterPosition]).name}")
//                File("${mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}", File(arrayPathImage[holder.adapterPosition]).name).delete()
            }
            arrayPathImage.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
        }

        holder.itemView.image.setOnClickListener {
            clickItemListener.onClickItemAdapter(holder.adapterPosition, arrayPathImage)
        }
    }

    override fun getItemCount(): Int = arrayPathImage.size

    fun setList(listImages: ArrayList<GalleryData>) {
        var pathUri = ""
        for (i in listImages) {
            pathUri += i.photoUri + "$"
        }
        arrayPathImage.clear()
        arrayPathImage = ArrayList(pathUri.split("$", ignoreCase = true))
        arrayPathImage.removeAt(arrayPathImage.size-1)
        notifyDataSetChanged()
    }

    fun getList() : ArrayList<String> = arrayPathImage

    fun getListForDelete() : ArrayList<String> = arrayPathImageForDelete

    fun setArrayPath(paths: String) {
        arrayPathImage.clear()
        arrayPathImage = ArrayList(paths.split("$", ignoreCase = true))
        arrayPathImage.removeAt(arrayPathImage.size-1)
        notifyDataSetChanged()
    }

    class ImagesViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
package com.shiryaev.schedule.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.davemorrissey.labs.subscaleview.ImageSource
import com.shiryaev.gallerypicker.utils.RunOnUiThread
import com.shiryaev.schedule.R
import kotlinx.android.synthetic.main.fr_item_show_picture.view.*
import org.jetbrains.anko.doAsync
import java.io.File

class ShowPictureAdapter(
    private val mContext: Context
) : RecyclerView.Adapter<ShowPictureAdapter.PagerVH>() {
    private var listPath: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(LayoutInflater.from(mContext).inflate(R.layout.fr_item_show_picture, parent, false))

    override fun onBindViewHolder(holder: PagerVH, position: Int) {
        doAsync {
            RunOnUiThread(mContext).safely {
                holder.itemView.progressBar.isVisible = true
                val bitmap = MediaStore.Images.Media.getBitmap(mContext.contentResolver, Uri.fromFile(File(listPath[holder.adapterPosition])))
                val ei = ExifInterface(listPath[holder.adapterPosition])
                val rotateBitmap: Bitmap? = when(ei.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
                )) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
                    ExifInterface.ORIENTATION_NORMAL -> bitmap
                    else -> bitmap
                }
                if (rotateBitmap != null) {
                    holder.itemView.photoView.setImage(ImageSource.bitmap(rotateBitmap))
                } else {
                    Toast.makeText(mContext, mContext.resources.getString(R.string.warning_load_image), Toast.LENGTH_LONG).show()
                }
                holder.itemView.progressBar.isVisible = false
            }
        }
    }

    override fun getItemCount(): Int = listPath.size

    fun setList(newListPath: ArrayList<String>) {
        listPath = newListPath
        notifyDataSetChanged()
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)
}
package com.example.gallerypicker.model.interactor

import android.content.Context
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.util.Log
import com.example.gallerypicker.model.GalleryImage
import java.io.File

class GalleryPicker(var ctx: Context) {
    fun getImages(): ArrayList<GalleryImage> {
        val images: ArrayList<GalleryImage> = ArrayList()
        try {
            if (isReadWritePermitted()) {
                val imagesProjection = arrayOf(MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media.DATE_MODIFIED,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.HEIGHT,
                    MediaStore.Images.Media.MIME_TYPE,
                    MediaStore.Images.Media.SIZE,
                    MediaStore.Images.Media.TITLE,
                    MediaStore.Images.Media.WIDTH,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.BUCKET_ID,
                    MediaStore.Images.Media.DATE_TAKEN,
                    MediaStore.Images.Media.DESCRIPTION,
                    MediaStore.Images.Media.IS_PRIVATE,
                    MediaStore.Images.Media.LATITUDE,
                    MediaStore.Images.Media.LONGITUDE,
                    MediaStore.Images.Media.MINI_THUMB_MAGIC,
                    MediaStore.Images.Media.ORIENTATION,
                    MediaStore.Images.Media.PICASA_ID)
                val imagesQueryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                val imagescursor = ctx.contentResolver.query(imagesQueryUri, imagesProjection, null, null, null)

                if (imagescursor != null && imagescursor.count > 0) {
                    if (imagescursor.moveToFirst()) {
                        do {
                            val image = GalleryImage()
                            image.ID = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media._ID))
                            image.DATA = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.DATA))
                            image.DATE_ADDED = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED))
                            image.DATE_MODIFIED = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED))
                            image.DISPLAY_NAME = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                            image.HEIGHT = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.HEIGHT))
                            image.MIME_TYPE = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE))
                            image.SIZE = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.SIZE))
                            image.TITLE = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.TITLE))
                            image.WIDTH = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.WIDTH))
                            image.BUCKET_DISPLAY_NAME = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                            image.BUCKET_ID = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID))
                            image.DATE_TAKEN = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN))
                            image.DESCRIPTION = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION))
                            image.IS_PRIVATE = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.IS_PRIVATE))
                            image.LATITUDE = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.LATITUDE))
                            image.LONGITUDE = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE))
                            image.MINI_THUMB_MAGIC = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.MINI_THUMB_MAGIC))
                            image.ORIENTATION = imagescursor.getString(imagescursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION))
                            image.PICASA_ID = imagescursor.getString(imagescursor.getColumnIndex(
                                MediaStore.Images.Media.PICASA_ID))
                            image.ALBUM_NAME = File(image.DATA).parentFile.name
                            images.add(image)
                        } while (imagescursor.moveToNext())
                    }
                    imagescursor.close()
                }
            }
        } catch (e: Exception) {
            Log.e("IMAGES", e.toString())
        } finally {
            return images
        }
    }

    private fun isReadWritePermitted(): Boolean = (ctx.checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ctx.checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
}
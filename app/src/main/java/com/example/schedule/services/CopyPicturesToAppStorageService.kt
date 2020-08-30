package com.example.schedule.services

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.ResultReceiver
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.JobIntentService
import com.example.schedule.util.RequestCode
import com.example.schedule.util.ServiceResultReceiver
import org.jetbrains.anko.longToast
import java.io.File
import java.io.FileOutputStream

class CopyPicturesToAppStorageService : JobIntentService() {

    private val TAG = CopyPicturesToAppStorageService::class.java.simpleName
    private var resultReceiver: ResultReceiver? = null

    override fun onHandleWork(intent: Intent) {
        resultReceiver = intent.getParcelableExtra(RECEIVER)!!

        if (resultReceiver == null) {
            Log.wtf(TAG, "No receiver received. There is nowhere to send the results !!")
        }

        val newPathUri = try {
            saveImage(intent.getStringExtra("pathUri")!!)
        } catch (e: Exception) {
            ""
        }
        val bundle = Bundle()
        intent.extras?.getInt("requestCode")?.let { bundle.putInt("requestCode", it) }
        bundle.putString("note", intent.getStringExtra("note"))
        bundle.putString("lesson", intent.getStringExtra("lesson"))
        bundle.putString("deadline", intent.getStringExtra("deadline"))
        intent.extras?.getInt("bgColor")?.let { bundle.putInt("bgColor", it) }
        bundle.putString("pathUri", newPathUri)
        if (intent.extras?.getInt("requestCode") != RequestCode.REQUEST_NOTE_ACTIVITY) {
            intent.extras?.getLong("itemId")?.let { bundle.putLong("itemId", it) }
        }
        resultReceiver!!.send(SHOW_RESULT, bundle)
    }

    companion object {
        const val RECEIVER = "receiver"
        private const val COPY_JOB_ID = 1000
        const val SHOW_RESULT = 123

        fun enqueueWork(context: Context, data: Intent, workerResultReceiver: ServiceResultReceiver) {
            val intent = Intent(context, CopyPicturesToAppStorageService::class.java)
            intent.putExtra(RECEIVER, workerResultReceiver)
            intent.putExtra("requestCode", data.extras?.getInt("requestCode"))
            intent.putExtra("note", data.getStringExtra("note"))
            intent.putExtra("lesson", data.getStringExtra("lesson"))
            intent.putExtra("deadline", data.getStringExtra("deadline"))
            intent.putExtra("bgColor", data.extras?.getInt("bgColor"))
            intent.putExtra("pathUri", data.getStringExtra("pathUri"))
            if (data.extras?.getInt("requestCode") != RequestCode.REQUEST_NOTE_ACTIVITY) {
                intent.putExtra("itemId", data.extras?.getLong("itemId"))
            }
            enqueueWork(context, CopyPicturesToAppStorageService::class.java, COPY_JOB_ID, intent)
        }
    }

    private fun saveImage(pathUti: String) : String {
        val arrayPath = ArrayList(pathUti.split("$", ignoreCase = true))
        arrayPath.removeAt(arrayPath.size - 1)
        var newArrayPath = ""
        for (path in arrayPath) {
            newArrayPath += "${galleryAddPic(path)}$"
        }
        return newArrayPath
    }

    private fun galleryAddPic(pickFilePath: String) : String {
        val f = File(pickFilePath)
        val contentUri = Uri.fromFile(f)
        val file = File(
            "${getExternalFilesDir(Environment.DIRECTORY_PICTURES)}", "PICTURE_${
                Regex(
                    "\\S+\\."
                ).find(f.name)?.value
            }jpg"
        )
        return try {
            val out = FileOutputStream(file)
            val bitmap = MediaStore.Images.Media.getBitmap(
                contentResolver,
                contentUri
            )
            val ei = ExifInterface(pickFilePath)
            val rotatedBitmap: Bitmap? = when (ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
                ExifInterface.ORIENTATION_NORMAL -> bitmap
                else -> bitmap
            }
            rotatedBitmap?.compress(Bitmap.CompressFormat.JPEG, 85, out)
            out.flush()
            out.close()
            file.absolutePath
        } catch (e: Exception) {
            ""
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }
}

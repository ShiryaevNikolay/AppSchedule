package com.shiryaev.gallerypicker.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.shiryaev.gallerypicker.R
import kotlinx.android.synthetic.main.fr_picker.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws

class PickerActivity : AppCompatActivity() {
    private val PERMISSIONS_CAMERA = 124
    var IMAGES_THRESHOLD = 0
    var REQUEST_RESULT_CODE = 101

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("theme_mode", false))
            setTheme(R.style.AppTheme_Dark)
        else
            setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.fr_picker)

        val i = intent
        IMAGES_THRESHOLD = i.getIntExtra("IMAGES_LIMIT", 0)
        REQUEST_RESULT_CODE = i.getIntExtra("REQUEST_RESULT_CODE", 0)

        camera.setOnClickListener {
            if (isCameraPermitted()) dispatchTakePictureIntent() else checkCameraPermission()
        }
    }

    private fun isCameraPermitted(): Boolean {
        val permission = Manifest.permission.CAMERA
        val cameraPermission = checkCallingOrSelfPermission(permission)
        return (cameraPermission == PackageManager.PERMISSION_GRANTED)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkCameraPermission(): Boolean {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSIONS_CAMERA)
        return true
    }

    val REQUEST_TAKE_PHOTO = 1
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
            }
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(this, "com.example.gallerypicker.fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) galleryAddPic()
    }

    private var mCurrentPhotoPath: String = ""

    private fun galleryAddPic() {
        val f = File(mCurrentPhotoPath)
        val contentUri = Uri.fromFile(f)
        val path = "${Environment.getExternalStorageDirectory()}${File.separator}Zoho Social${File.separator}media${File.separator}Zoho Social Images"
        val folder = File(path)
        if (!folder.exists()) folder.mkdirs()
        val file = File(path, "${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}_picture.jpg")
        val out = FileOutputStream(file)
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, contentUri)
        val ei = ExifInterface(mCurrentPhotoPath)
        val rotatedBitmap: Bitmap? = when (ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> null
        }
        rotatedBitmap?.compress(Bitmap.CompressFormat.JPEG, 70, out)
        out.close()
        ContentUris.parseId(Uri.parse(MediaStore.Images.Media.insertImage(contentResolver, file.absolutePath, file.name, file.name)))
        try {
            PhotosFragment().initViews()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }
}
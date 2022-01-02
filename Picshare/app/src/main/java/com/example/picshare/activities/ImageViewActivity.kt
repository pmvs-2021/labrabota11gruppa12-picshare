package com.example.picshare.activities

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.picshare.R
import com.example.picshare.service.ImageService
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.view.drawToBitmap
import com.example.picshare.service.ImageCache
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import java.io.OutputStream

class ImageViewActivity : AppCompatActivity() {
    lateinit var ivImage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        title = intent.getStringExtra("title")
        val imageId = intent.getIntExtra("image_id", 0)
        ivImage = findViewById(R.id.ivImage)
        val cacheBmp = ImageCache.downloadOrNull(applicationContext, imageId.toString())
        if (cacheBmp == null) {
            startImageDownload(imageId)
        } else {
            ivImage.background = BitmapDrawable(resources, cacheBmp)
        }
        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener { v ->
            onClick(v)
        }
    }

    private fun startImageDownload(imageId: Int) {
        println("FOR IMG: $imageId")
        val imageFuture = ImageService.getImage(imageId)
        val loadingSnackbar = Snackbar.make(
            ivImage, getString(R.string.loading),
            BaseTransientBottomBar.LENGTH_INDEFINITE
        )
        loadingSnackbar.show()
        val t = Thread {
            val resp = imageFuture.get()
            println("WHITE COUNT: " + resp.data.count { it.toInt() == -1 })
            val bmp = BitmapFactory.decodeByteArray(resp.data, 0, resp.data.size)
            runOnUiThread {
                ivImage.background = BitmapDrawable(resources, bmp)
            }
            loadingSnackbar.dismiss()
            ImageCache.upload(applicationContext, imageId.toString(), bmp)
        }
        t.start()
    }

    fun onClick(v: View?) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0
            )
        }
        val filename = "IMG_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream?
        var imageUri: Uri?
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.Video.Media.IS_PENDING, 0)
            }
        }
        val contentResolver = application.contentResolver

        contentResolver.also { resolver ->
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }

        fos?.use { ivImage.drawToBitmap().compress(Bitmap.CompressFormat.JPEG, 100, it) }
        contentValues.clear()
        val realUri = contentResolver.update(imageUri!!, contentValues, null, null)
        println(realUri)
        Snackbar.make(
            v!!, getString(R.string.successfully_saved), LENGTH_SHORT
        ).show()
    }

}
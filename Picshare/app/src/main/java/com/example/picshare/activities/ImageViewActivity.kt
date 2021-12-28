package com.example.picshare.activities

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.picshare.R
import com.example.picshare.service.ImageService
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import android.graphics.drawable.BitmapDrawable
import com.example.picshare.service.ImageCache

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
}
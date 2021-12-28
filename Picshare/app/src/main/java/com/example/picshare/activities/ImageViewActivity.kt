package com.example.picshare.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.picshare.R

class ImageViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        title = intent.getStringExtra("title")
        val imageId = intent.getIntExtra("image_id", 0)
        startImageDownload(imageId)
    }

    private fun startImageDownload(imageId: Int) {
//        TODO("Not yet implemented")
    }
}
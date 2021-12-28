package com.example.picshare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ImageViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        title = intent.getStringExtra("title")
    }
}
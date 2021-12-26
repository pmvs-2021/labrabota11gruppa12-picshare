package com.example.picshare.activities

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.byox.drawview.views.DrawView
import com.example.picshare.R
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import java.io.File
import java.io.FileOutputStream
import android.content.ContextWrapper
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.view.drawToBitmap
import com.example.picshare.Metadata
import com.example.picshare.service.ChatService
import com.example.picshare.service.ImageService
import com.example.picshare.service.SubscriberService
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.lang.Exception
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import java.io.OutputStream
import java.nio.ByteBuffer
import android.database.Cursor


class DrawActivity : AppCompatActivity() {
    private lateinit var ibtChats: ImageButton
    private lateinit var ibtOpen: ImageButton
    private lateinit var ibtSave: ImageButton
    private lateinit var ibtShare: ImageButton
    private lateinit var ibtSearch: ImageButton
    private lateinit var ibtColor: ImageButton
    private lateinit var dv: DrawView
    private lateinit var sb: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        bindLateinits()
        setListeners()
    }

    private fun bindLateinits() {
        ibtChats = findViewById(R.id.ibtSwitch)
        ibtOpen = findViewById(R.id.ibtOpen)
        ibtSave = findViewById(R.id.ibtSave)
        ibtShare = findViewById(R.id.ibtShare)
        ibtSearch = findViewById(R.id.ibtSearch)
        ibtColor = findViewById(R.id.btnColor)
        dv = findViewById(R.id.drawView)
        dv.backgroundColor = Color.WHITE
        sb = findViewById(R.id.seekBar)
    }

    private fun setListeners() {
        ibtSearch.setOnClickListener { v -> onSearchClick(v) }
        ibtSave.setOnClickListener { v -> onSaveClick(v) }
        ibtColor.setOnClickListener { v -> onColorClick(v) }
        ibtChats.setOnClickListener { v -> onChatClick(v) }
        sb.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                dv.drawWidth = progress * 2
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
        ibtShare.setOnClickListener { v -> onShareClick(v!!) }
    }

    private fun onSearchClick(v: View?) {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    private fun onChatClick(v: View?) {
        val intent = Intent(this, ChatsActivity::class.java)
        startActivity(intent)
    }

    private fun onColorClick(v: View?) {
        MaterialColorPickerDialog
            .Builder(this)
            .setColorShape(ColorShape.SQAURE)
            .setColorSwatch(ColorSwatch._300)
            .setColorListener { color, _ ->
                dv.drawColor = color
            }
            .show()
    }

    private fun onSaveClick(v: View?) {
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

        //use application context to get contentResolver
        val contentResolver = application.contentResolver

        contentResolver.also { resolver ->
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }

        fos?.use { dv.drawToBitmap().compress(Bitmap.CompressFormat.JPEG, 100, it) }
        contentValues.clear()
        val realUri = contentResolver.update(imageUri!!, contentValues, null, null)
        println(realUri)
        Snackbar.make(
            v!!, getString(R.string.successfully_saved), LENGTH_SHORT
        ).show()
    }

    private fun onShareClick(view: View) {
        val bmp = dv.drawToBitmap()
        println(bmp.byteCount)
        val buf = ByteBuffer.allocate(bmp.byteCount)
        bmp.copyPixelsToBuffer(buf)
        val futureRes = ImageService.addImage(buf.array())
        val futureSubscribers = SubscriberService.getSubscribers(Metadata.thisUser.id.toString())
        val t = Thread {
            val resp = futureRes.get()
            println(resp.toString())
            Snackbar.make(view, "Image sent to server", LENGTH_SHORT).show()
//            val subscribers = futureSubscribers.get().toCollection(ArrayList())
//            ChatService.sendMessage(subscribers, resp.getInt("id"))
//            Snackbar.make(view, "Sending to subscribers...", LENGTH_SHORT).show()
        }
        t.start()
    }
}
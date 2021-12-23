package com.example.picshare.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.drawToBitmap
import com.byox.drawview.views.DrawView
import com.example.picshare.R
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import java.io.File
import java.io.FileOutputStream


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
        val file = File("picture")

        try {
            file.createNewFile()

            val ostream = FileOutputStream(file)
            val bitmap = getBitmapFromView(dv)
            bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, ostream)
            ostream.flush()
            ostream.close()
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                "Error saving file: " + e.message,
                Toast.LENGTH_SHORT
            ).show();
        }
    }

    private fun getBitmapFromView(view: View): Bitmap? {
        val bitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
//        val state = dv.drawableState
//        savedInstanceState.putByteArray("pic", state.toDrawable())
        super.onSaveInstanceState(savedInstanceState)
    }

    @Override
    private fun onLoadInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val state = java.lang.Boolean.parseBoolean(savedInstanceState
                .getString("pic", ""))
            dv.drawToBitmap() //.drawableState = state
        }
    }
}
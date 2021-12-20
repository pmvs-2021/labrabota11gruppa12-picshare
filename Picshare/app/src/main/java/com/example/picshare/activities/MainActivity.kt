package com.example.picshare.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import com.example.picshare.R

class MainActivity : AppCompatActivity() {
    private lateinit var ibtChats: ImageButton
    private lateinit var ibtOpen: ImageButton
    private lateinit var ibtSave: ImageButton
    private lateinit var ibtShare: ImageButton
    private lateinit var ibtSearch: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindLateinits()
        setListeners()
    }

    private fun bindLateinits() {
        ibtChats = findViewById(R.id.ibtSwitch)
        ibtOpen = findViewById(R.id.ibtOpen)
        ibtSave = findViewById(R.id.ibtSave)
        ibtShare = findViewById(R.id.ibtShare)
        ibtSearch = findViewById(R.id.ibtSearch)
    }

    private fun setListeners() {
        ibtSearch.setOnClickListener { v -> onSearchClick(v) }
    }

    private fun onSearchClick(v: View?) {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
}
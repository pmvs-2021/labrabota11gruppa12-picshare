package com.example.picshare.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.picshare.Metadata.thisUser
import com.example.picshare.R
import com.example.picshare.domain.User
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar

class SearchActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var btFind: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        bindLateinits()
        setListeners()
    }

    private fun bindLateinits() {
        etUsername = findViewById(R.id.etUsername)
        btFind = findViewById(R.id.btFind)
    }

    private fun setListeners() {
        btFind.setOnClickListener{ v -> onFindClick(v) }
    }

    private fun onFindClick(v: View?) {
        val username = etUsername.text.toString()
        val user = serviceFindByUsername(username)
        if (user == null) {
            Snackbar.make(v!!, getString(R.string.user_not_found_msg), LENGTH_SHORT).show()
            return
        }
        if (serviceIsSubscriber(user.id, thisUser!!.id)){
            Snackbar.make(v!!, getString(R.string.already_subscribed_msg), LENGTH_SHORT).show()
            return
        }
        Snackbar.make(v!!, getString(R.string.successfully_subscribed_msg), LENGTH_SHORT).show()
    }

    private fun serviceIsSubscriber(id: Int, thisUserId: Int): Boolean {
        // TODO: create service call instead
        return true
    }

    private fun serviceFindByUsername(username: String): User? {
        // TODO: create service call instead
        return null
    }
}
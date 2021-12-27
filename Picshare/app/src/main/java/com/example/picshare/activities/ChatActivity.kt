package com.example.picshare.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.picshare.R
import com.example.picshare.domain.Message
import com.example.picshare.domain.User
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {
    lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        title = intent.getStringExtra("second_user_name")
        setUpViews()
    }

    private fun setUpViews() {
        val rvMain = findViewById<RecyclerView>(R.id.rvChat)
        val layoutManager = LinearLayoutManager(this)
        rvMain!!.layoutManager = layoutManager
        val messages = ArrayList<Message>()
        messages.add(
            Message(
                byteArrayOf(0, 1, 2), User(
                    0, "Екатерина Александровна ВРУБЛЕВСКАЯ -- " +
                            "СПЕЦИАЛИСТ ПО СИ ШАРПУ И ЖИЗНИ В ОБЩАГЕ", ""
                ), User(0, "Болван", ""),
                Calendar.getInstance()
            )
        )
        messages.add(
            Message(
                byteArrayOf(0, 1, 2), User(
                    0, "Екатерина Александровна ВРУБЛЕВСКАЯ -- " +
                            "СПЕЦИАЛИСТ ПО СИ ШАРПУ И ЖИЗНИ В ОБЩАГЕ", ""
                ), User(0, "Болван", ""),
                Calendar.getInstance()
            )
        )
        adapter = ChatAdapter(messages, this)
        rvMain.adapter = adapter
    }
}
package com.example.picshare.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.picshare.Metadata
import com.example.picshare.R
import com.example.picshare.domain.ChatName
import com.example.picshare.domain.User
import com.google.gson.Gson
import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ChatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)
        setUpViews()
        getSubscribers()
    }
    lateinit var adapter : ChatsAdapter
    private fun setUpViews() {
        val rvMain = findViewById<RecyclerView>(R.id.rvChats)
        val layoutManager = LinearLayoutManager(this)
        rvMain!!.layoutManager = layoutManager
        val chatNames = ArrayList<ChatName>()
        adapter = ChatsAdapter(chatNames, this)
        rvMain.adapter = adapter
    }

    private fun getSubscribers() {
        val id = Metadata.thisUser.id
        val url = String.format(
            "https://%s/chats?user=%s", Metadata.serverURL, id.toString()
        )

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                println(response)
                adapter.users.clear()
                val parsed = Gson().fromJson(response, Array<ChatNameResponse>::class.java)
                adapter.users.addAll(responseToChatName(parsed))
                adapter.notifyDataSetChanged()
            }
        ) { error -> Log.d("ERROR", error.toString()) }
        Metadata.requests.add(stringRequest)
    }

    private fun responseToChatName(responses: Array<ChatNameResponse>): MutableList<ChatName> {
        val converted= ArrayList<ChatName>()
        for (resp in responses) {
            val cal = Calendar.getInstance();
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
            //2021-12-23T13:10:27.40801Z
            val date = resp.time!!.substring(0, 10)
            val time = resp.time!!.substring(11, 18)
            cal.time = sdf.parse("$date $time")!!
            converted.add(ChatName(cal, resp.subscriber))
        }
        return converted
    }
}

class ChatNameResponse : Serializable {
    var time: String? = null
    var subscriber: User? = null
}
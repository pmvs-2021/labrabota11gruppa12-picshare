package com.example.picshare.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.picshare.Metadata
import com.example.picshare.R
import com.example.picshare.domain.Message
import com.example.picshare.domain.User
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {
    lateinit var adapter: ChatAdapter
    private var secondUserId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        title = intent.getStringExtra("second_user_name")
        secondUserId = intent.getIntExtra("second_user_id", -1)
        if (secondUserId == -1) {
            if (savedInstanceState == null) {
                println("PANIC!!!")
                return
            }
            title = savedInstanceState.getString("title")
            secondUserId = savedInstanceState.getInt("second_user_id")
        }
        setUpViews()
        getMessages()
    }

    private fun getMessages() {
        val id = Metadata.thisUser.id
        val url = String.format(
            "https://%s/chat?first=%s&second=%s",
            Metadata.serverURL,
            id.toString(),
            secondUserId.toString()
        )

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                println(response)
                adapter.messages.clear()
                val parsed = Gson().fromJson(response, Array<MessageResponse>::class.java)
                adapter.messages.addAll(responseToMessages(parsed))
                adapter.notifyDataSetChanged()
            }
        ) { error -> Log.d("ERROR", error.toString()) }
        Metadata.requests.add(stringRequest)
    }

    private fun setUpViews() {
        val rvMain = findViewById<RecyclerView>(R.id.rvChat)
        val layoutManager = LinearLayoutManager(this)
        rvMain!!.layoutManager = layoutManager
        val messages = ArrayList<Message>()
        adapter = ChatAdapter(messages, this)
        rvMain.adapter = adapter
    }

    private fun responseToMessages(responses: Array<MessageResponse>): MutableList<Message> {
        val converted = ArrayList<Message>()
        for (resp in responses) {
            val cal = Calendar.getInstance();
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
            val date = resp.time.substring(0, 10)
            val time = resp.time.substring(11, 18)
            cal.time = sdf.parse("$date $time")!!
            converted.add(Message(resp.image_id, resp.sender, resp.receiver ,cal))
        }
        return converted
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("title", title.toString())
        outState.putInt("second_user_id", secondUserId)
        super.onSaveInstanceState(outState)
    }
}

class MessageResponse(var image_id: Int, var sender: User?, var receiver: User?, var time: String)
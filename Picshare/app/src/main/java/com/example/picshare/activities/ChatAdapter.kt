package com.example.picshare.activities

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.picshare.R
import com.example.picshare.domain.Message
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(var messages: MutableList<Message>, var ctx: Context) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.image_card_layout, parent,
            false
        )
        view.setOnClickListener{
            val intent = Intent( ctx, ChatActivity::class.java)
            ctx.startActivity(intent)
        }
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val w = messages[position]
        holder.picture.setImageBitmap(BitmapFactory.decodeByteArray(w.image, 0, w.image!!.size))
        val dateFormat = if (!DateUtils.isToday(w.time!!.time.time)) {
            SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        } else {
            SimpleDateFormat("HH:mm", Locale.ENGLISH)
        }
        holder.tvTime.text = dateFormat.format(w.time!!.time)
    }



    override fun getItemCount(): Int {
        return messages.size
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var picture: ImageView = itemView.findViewById(R.id.picture)
        var tvTime: TextView = itemView.findViewById(R.id.tvTime)
    }
}
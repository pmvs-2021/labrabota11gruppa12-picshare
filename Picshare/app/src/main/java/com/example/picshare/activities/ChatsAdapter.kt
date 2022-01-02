package com.example.picshare.activities

import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.picshare.R
import com.example.picshare.domain.ChatName
import java.text.SimpleDateFormat
import java.util.*

class ChatsAdapter(var users: MutableList<ChatName>, var ctx: Context) :
    RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.card_layout, parent,
            false
        )

        return ChatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        val w = users[position]
        holder.tvUsername.text = w.subscriber!!.username
        val dateFormat = if (!DateUtils.isToday(w.time!!.time.time)) {
            SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        } else {
            SimpleDateFormat("HH:mm", Locale.ENGLISH)
        }
        holder.tvTime.text = dateFormat.format(w.time!!.time)
        holder.itemView.setOnClickListener {
            val intent = Intent(ctx, ChatActivity::class.java)
            intent.putExtra("second_user_name", w.subscriber!!.username)
            intent.putExtra("second_user_id", w.subscriber!!.id)
            ctx.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return users.size
    }

    class ChatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
        var tvTime: TextView = itemView.findViewById(R.id.tvTime)
        var view: View = itemView
    }
}


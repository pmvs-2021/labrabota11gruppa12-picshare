package com.example.picshare.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.format.DateUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.picshare.ImageViewActivity
import com.example.picshare.Metadata
import com.example.picshare.R
import com.example.picshare.domain.Message
import com.example.picshare.service.ImageCache
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(var messages: MutableList<Message>, var ctx: Context) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.image_card_layout, parent,
            false
        )
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val w = messages[position]
        if (ImageCache.contains(w.imageId)) {
            holder.picture.setImageBitmap(ImageCache.load(w.imageId))
        } else {
            holder.picture.setImageResource(R.drawable.photo)
        }
        val dateFormat = if (!DateUtils.isToday(w.time!!.time.time)) {
            SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        } else {
            SimpleDateFormat("HH:mm", Locale.ENGLISH)
        }
        if (w.sender!!.id == Metadata.thisUser.id) {
            holder.clCard.setBackgroundColor(ctx.getColor(R.color.blue_300))
        }
        holder.tvTime.text = dateFormat.format(w.time!!.time)
        holder.tvId.text = String.format(ctx.getString(R.string.number_sign_d), w.imageId)
        holder.clMain.setOnClickListener {
            val intent = Intent(ctx, ImageViewActivity::class.java)
            intent.putExtra("title", holder.tvId.text)
            ctx.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return messages.size
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var picture: ImageView = itemView.findViewById(R.id.picture)
        var tvTime: TextView = itemView.findViewById(R.id.tvTime)
        var tvId: TextView = itemView.findViewById(R.id.tvId)
        var clMain: LinearLayout = itemView.findViewById(R.id.clMain)
        var clCard: ConstraintLayout = itemView.findViewById(R.id.clCard)
    }
}
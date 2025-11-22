package com.example.email.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.email.R
import com.example.email.model.Email

class EmailAdapter(
    private val items: List<Email>
) : RecyclerView.Adapter<EmailAdapter.EmailViewHolder>() {

    inner class EmailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAvatar: TextView = itemView.findViewById(R.id.tvAvatar)
        val tvSender: TextView = itemView.findViewById(R.id.tvSender)
        val tvSubject: TextView = itemView.findViewById(R.id.tvSubject)
        val tvPreview: TextView = itemView.findViewById(R.id.tvPreview)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val imgStar: ImageView = itemView.findViewById(R.id.imgStar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_email, parent, false)
        return EmailViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
        val email = items[position]
        holder.tvSender.text = email.senderName
        holder.tvSubject.text = email.subject
        holder.tvPreview.text = email.preview
        holder.tvTime.text = email.time

        // Chữ cái đầu cho avatar
        holder.tvAvatar.text = email.senderName.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

        // Sao / không sao
        val starRes = if (email.isStarred)
            android.R.drawable.btn_star_big_on
        else
            android.R.drawable.btn_star_big_off
        holder.imgStar.setImageResource(starRes)

        holder.imgStar.setOnClickListener {
            email.isStarred = !email.isStarred
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = items.size
}

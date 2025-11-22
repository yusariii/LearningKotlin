package com.example.playstore.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playstore.R
import com.example.playstore.model.AppItem
import com.example.playstore.model.AppRow

class AppAdapter(
    private val apps: List<AppItem>,
    private val layoutType: AppRow.LayoutType
) : RecyclerView.Adapter<AppAdapter.AppViewHolder>() {

    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgIcon: ImageView = itemView.findViewById(R.id.imgIcon)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvCategory: TextView? = itemView.findViewById(R.id.tvCategory)
        val tvInfo: TextView? = itemView.findViewById(R.id.tvInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val layoutRes = when (layoutType) {
            AppRow.LayoutType.VERTICAL_LIST -> R.layout.item_app
            AppRow.LayoutType.HORIZONTAL_STRIP -> R.layout.item_app_small
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return AppViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = apps[position]

        holder.tvName.text = app.name

        holder.tvCategory?.text = app.category
        holder.tvInfo?.text = "${app.rating} ★ · ${app.sizeMb} MB"

        holder.imgIcon.setImageResource(R.mipmap.ic_launcher)
    }

    override fun getItemCount(): Int = apps.size
}

package com.example.file_management

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FileAdapter(
    private val items: List<File>,
    private val onItemClick: (File) -> Unit,
    private val onItemLongClick: (File, View) -> Unit
) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgIcon: ImageView = itemView.findViewById(R.id.imgIcon)
        val tvName: TextView = itemView.findViewById(R.id.tvName)

        init {
            itemView.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onItemClick(items[pos])
                }
            }
            itemView.setOnLongClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onItemLongClick(items[pos], it)
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_file, parent, false)
        return FileViewHolder(v)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val f = items[position]
        holder.tvName.text = f.name

        val iconRes = if (f.isDirectory) {
            android.R.drawable.ic_menu_sort_by_size
        } else {
            android.R.drawable.ic_menu_agenda
        }
        holder.imgIcon.setImageResource(iconRes)
    }

    override fun getItemCount() = items.size
}

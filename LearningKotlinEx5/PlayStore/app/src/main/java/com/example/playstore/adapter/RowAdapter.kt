package com.example.playstore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playstore.R
import com.example.playstore.model.AppRow

class RowAdapter(
    private val rows: List<AppRow>
) : RecyclerView.Adapter<RowAdapter.RowViewHolder>() {

    inner class RowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRowTitle: TextView = itemView.findViewById(R.id.tvRowTitle)
        val rvApps: RecyclerView = itemView.findViewById(R.id.rvApps)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        return RowViewHolder(view)
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        val row = rows[position]
        holder.tvRowTitle.text = row.title

        val context = holder.itemView.context
        val orientation = when (row.layoutType) {
            AppRow.LayoutType.VERTICAL_LIST   -> LinearLayoutManager.VERTICAL
            AppRow.LayoutType.HORIZONTAL_STRIP -> LinearLayoutManager.HORIZONTAL
        }

        holder.rvApps.layoutManager = LinearLayoutManager(context, orientation, false)
        holder.rvApps.adapter = AppAdapter(row.apps, row.layoutType)
        holder.rvApps.isNestedScrollingEnabled = false
    }

    override fun getItemCount(): Int = rows.size
}

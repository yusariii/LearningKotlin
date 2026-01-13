package com.example.myapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class StudentAdapter(
    private var studentList: List<Student>,
    private val onClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>(), Filterable {

    private var studentListFiltered: List<Student> = studentList

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgThumb: ImageView = itemView.findViewById(R.id.imgThumb)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvMssv: TextView = itemView.findViewById(R.id.tvMssv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentListFiltered[position]
        holder.tvName.text = student.hoten
        holder.tvMssv.text = student.mssv

        val fullUrl = "https://lebavui.io.vn${student.thumbnail}"

        Glide.with(holder.itemView.context)
            .load(fullUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.imgThumb)

        holder.itemView.setOnClickListener { onClick(student) }
    }

    override fun getItemCount(): Int = studentListFiltered.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<Student>) {
        studentList = newList
        studentListFiltered = newList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                studentListFiltered = if (charString.isEmpty()) {
                    studentList
                } else {
                    studentList.filter {
                        it.hoten.contains(charString, true) || it.mssv.contains(charString, true)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = studentListFiltered
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                studentListFiltered = results?.values as List<Student>
                notifyDataSetChanged()
            }
        }
    }
}
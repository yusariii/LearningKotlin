package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val imgDetail: ImageView = findViewById(R.id.imgDetail)
        val tvDetailName: TextView = findViewById(R.id.tvDetailName)
        val tvDetailMssv: TextView = findViewById(R.id.tvDetailMssv)
        val tvDetailDob: TextView = findViewById(R.id.tvDetailDob)
        val tvDetailEmail: TextView = findViewById(R.id.tvDetailEmail)

        val student = intent.getSerializableExtra("student_data") as? Student

        student?.let {
            tvDetailName.text = "Họ tên: ${it.hoten}"
            tvDetailMssv.text = "MSSV: ${it.mssv}"
            tvDetailDob.text = "Ngày sinh: ${it.ngaysinh}"
            tvDetailEmail.text = "Email: ${it.email}"

            val fullUrl = "https://lebavui.io.vn${it.thumbnail}"
            Glide.with(this).load(fullUrl).into(imgDetail)
        }
    }
}
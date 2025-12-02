package com.example.dssv.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dssv.R
import com.example.dssv.repository.StudentRepository

class StudentDetailActivity : AppCompatActivity() {

    private lateinit var edtId: EditText
    private lateinit var edtName: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtAddress: EditText
    private lateinit var btnUpdate: Button

    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_detail)

        edtId = findViewById(R.id.edtId)
        edtName = findViewById(R.id.edtName)
        edtPhone = findViewById(R.id.edtPhone)
        edtAddress = findViewById(R.id.edtAddress)
        btnUpdate = findViewById(R.id.btnUpdate)

        position = intent.getIntExtra("position", -1)
        if (position != -1) {
            val s = StudentRepository.students[position]
            edtId.setText(s.id)
            edtName.setText(s.name)
            edtPhone.setText(s.phone)
            edtAddress.setText(s.address)
        }

        btnUpdate.setOnClickListener {
            if (position == -1) return@setOnClickListener

            val id = edtId.text.toString().trim()
            val name = edtName.text.toString().trim()
            val phone = edtPhone.text.toString().trim()
            val address = edtAddress.text.toString().trim()

            if (id.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "MSSV và Họ tên không được trống", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val s = StudentRepository.students[position]
            s.id = id
            s.name = name
            s.phone = phone
            s.address = address

            Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}

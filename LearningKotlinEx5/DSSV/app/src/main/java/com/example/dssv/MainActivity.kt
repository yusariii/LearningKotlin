package com.example.dssv

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dssv.adapter.StudentAdapter
import com.example.dssv.model.Student

class MainActivity : AppCompatActivity() {

    private lateinit var edtId: EditText
    private lateinit var edtName: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnUpdate: Button
    private lateinit var rvStudents: RecyclerView

    private val students = mutableListOf<Student>()
    private lateinit var adapter: StudentAdapter

    private var selectedPos: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupRecyclerView()
        setupEvents()
    }

    private fun initViews() {
        edtId = findViewById(R.id.edtId)
        edtName = findViewById(R.id.edtName)
        btnAdd = findViewById(R.id.btnAdd)
        btnUpdate = findViewById(R.id.btnUpdate)
        rvStudents = findViewById(R.id.rvStudents)
    }

    private fun setupRecyclerView() {
        adapter = StudentAdapter(
            students,
            onItemClick = { student, position ->
                edtId.setText(student.id)
                edtName.setText(student.name)
                selectedPos = position
            },
            onDeleteClick = { position ->
                students.removeAt(position)
                adapter.notifyItemRemoved(position)
                if (position == selectedPos) {
                    clearInput()
                    selectedPos = -1
                }
            }
        )

        rvStudents.layoutManager = LinearLayoutManager(this)
        rvStudents.adapter = adapter
    }

    private fun setupEvents() {
        btnAdd.setOnClickListener {
            val id = edtId.text.toString().trim()
            val name = edtName.text.toString().trim()

            if (id.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Nhập đủ MSSV và Họ tên", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            students.add(Student(id, name))
            adapter.notifyItemInserted(students.size - 1)
            clearInput()
            selectedPos = -1
        }

        btnUpdate.setOnClickListener {
            if (selectedPos == -1) {
                Toast.makeText(this, "Chọn 1 sinh viên để cập nhật", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = edtId.text.toString().trim()
            val name = edtName.text.toString().trim()

            if (id.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            students[selectedPos].id = id
            students[selectedPos].name = name
            adapter.notifyItemChanged(selectedPos)
            clearInput()
            selectedPos = -1
        }
    }

    private fun clearInput() {
        edtId.text.clear()
        edtName.text.clear()
    }
}

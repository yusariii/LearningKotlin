package com.example.dssv

import android.annotation.SuppressLint
import com.example.dssv.activity.AddStudentActivity
import com.example.dssv.activity.StudentDetailActivity
import com.example.dssv.adapter.StudentAdapter
import com.example.dssv.repository.StudentRepository
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var rvStudents: RecyclerView
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        rvStudents = findViewById(R.id.rvStudents)
        rvStudents.layoutManager = LinearLayoutManager(this)

        adapter = StudentAdapter(StudentRepository.students) { position ->
            val intent = Intent(this, StudentDetailActivity::class.java)
            intent.putExtra("position", position)
            startActivity(intent)
        }
        rvStudents.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                startActivity(Intent(this, AddStudentActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


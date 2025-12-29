package com.example.dssv.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dssv.database.StudentDbHelper

class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = StudentDbHelper(application.applicationContext)

    private val _students = MutableLiveData<MutableList<Student>>()
    val students: LiveData<MutableList<Student>> get() = _students

    init {
        loadFromDb()
    }

    private fun loadFromDb() {
        _students.value = dbHelper.getAllStudents()
    }

    fun getStudentAt(index: Int): Student? {
        return _students.value?.getOrNull(index)
    }

    fun addStudent(student: Student): Boolean {
        val result = dbHelper.insertStudent(student)
        return if (result != -1L) {
            loadFromDb()
            true
        } else {
            false
        }
    }

    fun updateStudent(index: Int, newStudent: Student): Boolean {
        val list = _students.value ?: return false
        if (index !in list.indices) return false

        val originalId = list[index].id

        val rows = dbHelper.updateStudentById(originalId, newStudent)
        return if (rows > 0) {
            loadFromDb()
            true
        } else {
            false
        }
    }

    fun deleteStudent(index: Int): Boolean {
        val list = _students.value ?: return false
        if (index !in list.indices) return false

        val id = list[index].id
        val rows = dbHelper.deleteStudentById(id)
        return if (rows > 0) {
            loadFromDb()
            true
        } else {
            false
        }
    }
}

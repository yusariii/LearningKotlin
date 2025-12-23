package com.example.dssv.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StudentViewModel : ViewModel() {

    private val _students = MutableLiveData(
        mutableListOf(
            Student("20200001", "Nguyễn Văn A", "0900000001", "Hà Nội"),
            Student("20200002", "Trần Thị B", "0900000002", "TP.HCM"),
            Student("20200003", "Lê Văn C", "0900000003", "Đà Nẵng")
        )
    )
    val students: LiveData<MutableList<Student>> get() = _students

    fun getStudentAt(index: Int): Student? {
        return _students.value?.getOrNull(index)
    }

    fun addStudent(student: Student) {
        val list = _students.value ?: mutableListOf()
        list.add(student)
        _students.value = list
    }

    fun updateStudent(index: Int, student: Student) {
        val list = _students.value ?: return
        if (index in list.indices) {
            list[index] = student
            _students.value = list
        }
    }
}

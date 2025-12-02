package com.example.dssv.repository

import com.example.dssv.model.Student

object StudentRepository {
    val students = mutableListOf<Student>(
        Student("20200001", "Nguyễn Văn A", "0900000001", "Hà Nội"),
        Student("20200002", "Trần Thị B", "0900000002", "Hồ Chí Minh"),
        Student("20200003", "Lê Văn C", "0900000003", "Đà Nẵng")
    )
}
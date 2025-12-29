package com.example.dssv.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.dssv.model.Student

class StudentDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "students.db"
        const val DATABASE_VERSION = 1

        const val TABLE_NAME = "students"
        const val COLUMN_ID = "student_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_ADDRESS = "address"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val sql = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_PHONE TEXT,
                $COLUMN_ADDRESS TEXT
            )
        """.trimIndent()
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun getAllStudents(): MutableList<Student> {
        val list = mutableListOf<Student>()
        val db = readableDatabase

        val cursor = db.query(
            TABLE_NAME,
            null,
            null, null, null, null,
            COLUMN_NAME
        )

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getString(it.getColumnIndexOrThrow(COLUMN_ID))
                val name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME))
                val phone = it.getString(it.getColumnIndexOrThrow(COLUMN_PHONE)) ?: ""
                val address = it.getString(it.getColumnIndexOrThrow(COLUMN_ADDRESS)) ?: ""

                list.add(Student(id, name, phone, address))
            }
        }
        return list
    }

    fun insertStudent(student: Student): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, student.id)
            put(COLUMN_NAME, student.name)
            put(COLUMN_PHONE, student.phone)
            put(COLUMN_ADDRESS, student.address)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun updateStudentById(originalId: String, student: Student): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, student.id)
            put(COLUMN_NAME, student.name)
            put(COLUMN_PHONE, student.phone)
            put(COLUMN_ADDRESS, student.address)
        }
        return db.update(
            TABLE_NAME,
            values,
            "$COLUMN_ID = ?",
            arrayOf(originalId)
        )
    }

    fun deleteStudentById(id: String): Int {
        val db = writableDatabase
        return db.delete(
            TABLE_NAME,
            "$COLUMN_ID = ?",
            arrayOf(id)
        )
    }
}

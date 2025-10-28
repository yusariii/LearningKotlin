package com.example.learningkotlinex3

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.view.isGone
import androidx.core.graphics.drawable.toDrawable

class MainActivity : AppCompatActivity() {
    private lateinit var inputFirstName: EditText
    private lateinit var inputLastName: EditText
    private lateinit var inputBirthday: EditText
    private lateinit var inputAddress: EditText
    private lateinit var inputEmail: EditText
    private lateinit var btnSelectDate: Button
    private lateinit var calendarView: CalendarView
    private lateinit var btnRegister: Button
    private lateinit var checkboxTerms: CheckBox
    private lateinit var radioGroupGender: RadioGroup

    private var defaultEditTextBackground: Drawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        inputFirstName = findViewById(R.id.input_first_name)
        inputLastName = findViewById(R.id.input_last_name)
        inputBirthday = findViewById(R.id.input_birthday)
        inputAddress = findViewById(R.id.input_address)
        inputEmail = findViewById(R.id.input_email)
        btnSelectDate = findViewById(R.id.btn_select_date)
        btnRegister = findViewById(R.id.btn_register)
        checkboxTerms = findViewById(R.id.checkbox_terms)
        radioGroupGender = findViewById(R.id.radio_group_gender)

        calendarView = findViewById(R.id.calendar_view)

        defaultEditTextBackground = inputFirstName.background

        setupDateSelection()

        btnRegister.setOnClickListener {
            validateAndRegister()
        }
    }

    private fun setupDateSelection() {
        btnSelectDate.setOnClickListener {
            if (calendarView.isGone) {
                calendarView.visibility = View.VISIBLE
            } else {
                calendarView.visibility = View.GONE
            }
        }

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            inputBirthday.setText(dateFormat.format(selectedDate.time))

            calendarView.visibility = View.GONE
        }
    }

    private fun validateAndRegister() {
        var isValid = true

        restoreDefaultBackground(inputFirstName)
        restoreDefaultBackground(inputLastName)
        restoreDefaultBackground(inputBirthday)
        restoreDefaultBackground(inputAddress)
        restoreDefaultBackground(inputEmail)

        if (inputFirstName.text.isNullOrBlank()) {
            markAsInvalid(inputFirstName)
            isValid = false
        }
        if (inputLastName.text.isNullOrBlank()) {
            markAsInvalid(inputLastName)
            isValid = false
        }
        if (inputBirthday.text.isNullOrBlank()) {
            markAsInvalid(inputBirthday)
            isValid = false
        }
        if (inputAddress.text.isNullOrBlank()) {
            markAsInvalid(inputAddress)
            isValid = false
        }
        if (inputEmail.text.isNullOrBlank()) {
            markAsInvalid(inputEmail)
            isValid = false
        }

        if (!checkboxTerms.isChecked) {
            checkboxTerms.setTextColor(Color.RED)
            isValid = false
        } else {
            checkboxTerms.setTextColor(ContextCompat.getColor(this, android.R.color.black))
        }

        if (isValid) {
            val gender = if (radioGroupGender.checkedRadioButtonId == R.id.radio_male) "Male" else "Female"

            val message = "Registration Successful!\n" +
                    "Name: ${inputFirstName.text} ${inputLastName.text}\n" +
                    "Gender: $gender\n" +
                    "Birthday: ${inputBirthday.text}\n" +
                    "Address: ${inputAddress.text}\n" +
                    "Email: ${inputEmail.text}"

            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Please fill in all required fields!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun markAsInvalid(editText: EditText) {
        val redColor = ContextCompat.getColor(this, android.R.color.holo_red_light)
        editText.background = redColor.toDrawable()
    }

    private fun restoreDefaultBackground(editText: EditText) {
        editText.background = defaultEditTextBackground
        if (editText.id != R.id.checkbox_terms) {
            checkboxTerms.setTextColor(ContextCompat.getColor(this, android.R.color.black))
        }
    }
}
package com.example.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var tvResult: TextView
    private lateinit var tvExpression: TextView

    private var currentNumber: String = "0"
    private var firstOperand: Int = 0
    private var operator: String? = null
    private var waitingForNewOperand: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tvResult)
        tvExpression = findViewById(R.id.tvExpression)

        setupAllButtonListeners()
        updateResultDisplay()
        updateExpressionDisplay()
    }

    @SuppressLint("SetTextI18n")
    private fun updateExpressionDisplay() {
        if (operator != null) {
            tvExpression.text = "$firstOperand $operator "
        } else {
            tvExpression.text = ""
        }
    }

    private fun setupAllButtonListeners() {
        findViewById<Button>(R.id.btn_ce).setOnClickListener(this)
        findViewById<Button>(R.id.btn_c).setOnClickListener(this)
        findViewById<Button>(R.id.btn_bs).setOnClickListener(this)
        findViewById<Button>(R.id.btn_divide).setOnClickListener(this)

        findViewById<Button>(R.id.btn_seven).setOnClickListener(this)
        findViewById<Button>(R.id.btn_eight).setOnClickListener(this)
        findViewById<Button>(R.id.btn_nine).setOnClickListener(this)
        findViewById<Button>(R.id.btn_multiply).setOnClickListener(this)

        findViewById<Button>(R.id.btn_four).setOnClickListener(this)
        findViewById<Button>(R.id.btn_five).setOnClickListener(this)
        findViewById<Button>(R.id.btn_six).setOnClickListener(this)
        findViewById<Button>(R.id.btn_subtract).setOnClickListener(this)

        findViewById<Button>(R.id.btn_one).setOnClickListener(this)
        findViewById<Button>(R.id.btn_two).setOnClickListener(this)
        findViewById<Button>(R.id.btn_three).setOnClickListener(this)
        findViewById<Button>(R.id.btn_add).setOnClickListener(this)

        findViewById<Button>(R.id.btn_sign).setOnClickListener(this)
        findViewById<Button>(R.id.btn_zero).setOnClickListener(this)
        findViewById<Button>(R.id.btn_equal).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v is Button) {
            when (val buttonText = v.text.toString()) {
                in "0".."9" -> onDigit(buttonText)
                "+", "-", "x", "/" -> onOperator(buttonText)
                "=" -> onEqual()
                "+/-" -> onToggleSign()
                "C" -> onClear()
                "CE" -> onClearEntry()
                "BS" -> onBackspace()
            }

            updateResultDisplay()
            updateExpressionDisplay()
        }
    }

    private fun updateResultDisplay() {
        tvResult.text = currentNumber
    }

    private fun onDigit(digit: String) {
        if (waitingForNewOperand) {
            currentNumber = digit
            waitingForNewOperand = false
        } else {
            if (currentNumber == "0" && digit != "0") {
                currentNumber = digit
            } else if (currentNumber != "0") {
                currentNumber += digit
            }
        }
    }

    private fun onOperator(nextOperator: String) {
        if (!waitingForNewOperand) {
            val currentOperand = currentNumber.toIntOrNull() ?: return

            if (operator == null) {
                firstOperand = currentOperand
            } else {
                firstOperand = performCalculation(firstOperand, currentOperand, operator!!)
                currentNumber = firstOperand.toString()
            }
        }

        operator = nextOperator
        waitingForNewOperand = true
    }

    @SuppressLint("SetTextI18n")
    private fun onEqual() {
        val secondOperand = currentNumber.toIntOrNull() ?: return

        if (operator != null && !waitingForNewOperand) {
            tvExpression.text = "$firstOperand $operator $secondOperand ="
            val result = performCalculation(firstOperand, secondOperand, operator!!)
            currentNumber = result.toString()

            firstOperand = result
            operator = null
            waitingForNewOperand = true
        }
    }

    private fun performCalculation(op1: Int, op2: Int, op: String): Int {
        return when (op) {
            "+" -> op1 + op2
            "-" -> op1 - op2
            "x" -> op1 * op2
            "/" -> if (op2 != 0) op1 / op2 else 0
            else -> op2
        }
    }

    private fun onClear() {
        currentNumber = "0"
        firstOperand = 0
        operator = null
        waitingForNewOperand = true
        tvExpression.text = ""
    }

    private fun onClearEntry() {
        currentNumber = "0"
        waitingForNewOperand = true
    }

    private fun onBackspace() {
        if (!waitingForNewOperand) {
            if (currentNumber.length > 1 && currentNumber != "0") {
                currentNumber = currentNumber.dropLast(1)
            } else {
                currentNumber = "0"
                waitingForNewOperand = true
            }
        }
    }

    private fun onToggleSign() {
        val num = currentNumber.toIntOrNull()
        if (num != null && num != 0) {
            currentNumber = (-num).toString()
        }
        waitingForNewOperand = false
    }
}
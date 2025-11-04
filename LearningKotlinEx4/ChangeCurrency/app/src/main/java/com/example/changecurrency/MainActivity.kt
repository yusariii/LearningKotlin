package com.example.changecurrency

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.RoundingMode
import androidx.core.graphics.drawable.toDrawable

class MainActivity : AppCompatActivity() {
    private lateinit var spFrom: Spinner
    private lateinit var spTo: Spinner
    private lateinit var etFrom: EditText
    private lateinit var etTo: EditText
    private lateinit var tvRate: TextView

    private val currencies = listOf(
        "USD", "EUR", "VND", "JPY", "GBP", "CNY",
        "KRW", "AUD", "CAD", "SGD", "THB", "IDR"
    )
    private val ratesToUSD = mapOf(
        "USD" to 1.0,
        "EUR" to 1.08,
        "VND" to 0.000039,
        "JPY" to 0.0065,
        "GBP" to 1.26,
        "CNY" to 0.14,
        "KRW" to 0.00073,
        "AUD" to 0.66,
        "CAD" to 0.73,
        "SGD" to 0.74,
        "THB" to 0.027,
        "IDR" to 0.000061
    )

    private var updatingFrom = false
    private var updatingTo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spFrom = findViewById(R.id.spFrom)
        spTo   = findViewById(R.id.spTo)
        etFrom = findViewById(R.id.etFrom)
        etTo   = findViewById(R.id.etTo)
        tvRate = findViewById(R.id.tvRate)

        setupSpinners()
        setupTextWatchers()
        updateRateLabel()
    }
    private fun setupSpinners() {
        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            currencies
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent) as TextView
                v.setTextColor(Color.BLACK)
                return v
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getDropDownView(position, convertView, parent) as TextView
                v.setTextColor(Color.BLACK)
                return v
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spFrom.setPopupBackgroundDrawable(Color.WHITE.toDrawable())
        spTo.setPopupBackgroundDrawable(Color.WHITE.toDrawable())

        spFrom.adapter = adapter
        spTo.adapter   = adapter

        spFrom.setSelection(currencies.indexOf("USD"))
        spTo.setSelection(currencies.indexOf("VND"))

        val onChange = { _: Any ->
            updateRateLabel()
            recalcFromTo()
        }

        spFrom.onItemSelectedListener = SimpleOnItemSelected { onChange(Unit) }
        spTo.onItemSelectedListener = SimpleOnItemSelected { onChange(Unit) }
    }

    private fun setupTextWatchers() {
        etFrom.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (updatingTo) return
                updatingFrom = true
                convertAtoB(etFrom, etTo, spFrom.selectedItem.toString(), spTo.selectedItem.toString())
                updatingFrom = false
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        etTo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (updatingFrom) return
                updatingTo = true
                convertAtoB(etTo, etFrom, spTo.selectedItem.toString(), spFrom.selectedItem.toString())
                updatingTo = false
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun convertAtoB(src: EditText, dst: EditText, curSrc: String, curDst: String) {
        val txt = src.text.toString()
        if (txt.isBlank() || txt == "." || txt == "-") {
            dst.setText("")
            return
        }
        val amount = txt.toBigDecimalOrNull() ?: run {
            dst.setText("")
            return
        }

        val res = convert(amount, curSrc, curDst)
        dst.setText(formatDecimal(res))
        dst.setSelection(dst.text?.length ?: 0)
    }

    private fun convert(amount: BigDecimal, from: String, to: String): BigDecimal {
        val usdPerFrom = ratesToUSD[from] ?: 1.0
        val usdPerTo   = ratesToUSD[to]   ?: 1.0
        val result = amount.toDouble() * (usdPerFrom / usdPerTo)
        return BigDecimal(result).setScale(6, RoundingMode.HALF_UP)
    }

    private fun recalcFromTo() {
        if (etFrom.hasFocus() || !etTo.hasFocus()) {
            convertAtoB(etFrom, etTo, spFrom.selectedItem.toString(), spTo.selectedItem.toString())
        } else {
            convertAtoB(etTo, etFrom, spTo.selectedItem.toString(), spFrom.selectedItem.toString())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateRateLabel() {
        val from = spFrom.selectedItem.toString()
        val to = spTo.selectedItem.toString()
        val one = convert(BigDecimal.ONE, from, to)
        tvRate.text = "Rate: 1 $from = ${formatDecimal(one)} $to"
    }

    private fun formatDecimal(x: BigDecimal): String {
        return x.stripTrailingZeros().toPlainString()
    }
}

class SimpleOnItemSelected(
    private val onSelected: (position: Int) -> Unit
) : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onSelected(position)
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {}
}

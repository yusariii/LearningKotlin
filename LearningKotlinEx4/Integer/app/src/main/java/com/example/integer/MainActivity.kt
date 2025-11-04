package com.example.integer

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var etLimit: EditText
    private lateinit var rgType: RadioGroup
    private lateinit var rgType2: RadioGroup
    private lateinit var lvNumbers: ListView
    private lateinit var tvEmpty: TextView
    private lateinit var adapter: ArrayAdapter<String>

    private enum class Kind { ODD, PRIME, PERFECT, EVEN, SQUARE, FIBO }
    private var currentKind = Kind.ODD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etLimit = findViewById(R.id.etLimit)
        rgType  = findViewById(R.id.rgType)
        rgType2 = findViewById(R.id.rgType2)
        lvNumbers = findViewById(R.id.lvNumbers)
        tvEmpty   = findViewById(R.id.tvEmpty)

        adapter = BlackTextArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        lvNumbers.adapter = adapter

        rgType.setOnCheckedChangeListener { g, id -> onChecked(g, id) }
        rgType2.setOnCheckedChangeListener { g, id -> onChecked(g, id) }

        etLimit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = refreshList()
            override fun afterTextChanged(s: Editable?) {}
        })

        refreshList()
    }

    private fun onChecked(group: RadioGroup, checkedId: Int) {
        if (checkedId == -1) return

        val other = if (group === rgType) rgType2 else rgType
        other.setOnCheckedChangeListener(null)
        other.clearCheck()
        other.setOnCheckedChangeListener { g, id -> onChecked(g, id) }

        currentKind = when (checkedId) {
            R.id.rbOdd     -> Kind.ODD
            R.id.rbPrime   -> Kind.PRIME
            R.id.rbPerfect -> Kind.PERFECT
            R.id.rbEven    -> Kind.EVEN
            R.id.rbSquare  -> Kind.SQUARE
            R.id.rbFibo    -> Kind.FIBO
            else -> currentKind
        }
        refreshList()
    }

    private fun refreshList() {
        val n = etLimit.text.toString().toLongOrNull() ?: 0L
        val res = buildList(n, currentKind)

        adapter.clear()
        if (res.isEmpty()) {
            lvNumbers.visibility = View.GONE
            tvEmpty.visibility = View.VISIBLE
        } else {
            lvNumbers.visibility = View.VISIBLE
            tvEmpty.visibility = View.GONE
            adapter.addAll(res.map { it.toString() })
        }
        adapter.notifyDataSetChanged()
    }

    private fun buildList(limit: Long, kind: Kind): List<Long> {
        if (limit <= 0) return emptyList()
        val ans = mutableListOf<Long>()
        when (kind) {
            Kind.ODD -> { var x = 1L; while (x < limit) { ans += x; x += 2 } }
            Kind.EVEN -> { var x = 0L; while (x < limit) { ans += x; x += 2 } }
            Kind.PRIME -> {
                val N = limit - 1
                if (N >= 2) {
                    val isPrime = BooleanArray((N + 1).toInt()) { true }
                    isPrime[0] = false; isPrime[1] = false
                    var p = 2
                    while (p.toLong() * p <= N) {
                        if (isPrime[p]) {
                            var q = p * p
                            while (q <= N.toInt()) { isPrime[q] = false; q += p }
                        }
                        p++
                    }
                    for (i in 2..N.toInt()) if (isPrime[i]) ans += i.toLong()
                }
            }
            Kind.SQUARE -> { var r = 1L; while (r * r < limit) { ans += r * r; r++ } }
            Kind.PERFECT -> {
                val candidates = longArrayOf(6, 28, 496, 8128, 33550336)
                for (x in candidates) if (x < limit) ans += x
            }
            Kind.FIBO -> {
                var a = 0L; var b = 1L
                while (a < limit) { ans += a; val c = a + b; a = b; b = c }
            }
        }
        return ans
    }
}

class BlackTextArrayAdapter(context: Context, resource: Int, objects: MutableList<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)

        val textView = view.findViewById<TextView>(android.R.id.text1)

        textView.setTextColor(ContextCompat.getColor(context, android.R.color.black))

        return view
    }
}
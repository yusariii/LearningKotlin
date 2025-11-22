package com.example.email

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.email.adapter.EmailAdapter
import com.example.email.model.Email
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var rvEmails: RecyclerView
    private lateinit var fabCompose: FloatingActionButton
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        rvEmails = findViewById(R.id.rvEmails)
        fabCompose = findViewById(R.id.fabCompose)

        val list = createDummyEmails()
        rvEmails.layoutManager = LinearLayoutManager(this)
        rvEmails.adapter = EmailAdapter(list)

        fabCompose.setOnClickListener {
            Toast.makeText(this, "Compose new mail", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createDummyEmails(): List<Email> {
        return listOf(
            Email("Edurila.com",  "$19 Only (First 10 spots)", "Are you looking to learn Web Design?", "12:34 PM"),
            Email("Chris Abad",   "Help make Campaign Monitor better", "Let us know your thoughts!", "11:22 AM"),
            Email("Tuto.com",     "8h de formation gratuite", "Photoshop, SEO, Blender, CSS, WordPress…", "11:04 AM"),
            Email("support",      "Suivi de vos services", "SAS OVH - http://www.ovh.com", "10:26 AM"),
            Email("Matt from Ionic", "The New Ionic Creator Is Here!", "Announcing the all-new Creator builder", "9:15 AM", isStarred = true)
        )
    }
}

package com.example.playstore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playstore.adapter.RowAdapter
import com.example.playstore.model.AppItem
import com.example.playstore.model.AppRow
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var rvRows: RecyclerView
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        rvRows = findViewById(R.id.rvRows)
        rvRows.layoutManager = LinearLayoutManager(this)

        rvRows.adapter = RowAdapter(createDummyData())
    }

    private fun createDummyData(): List<AppRow> {
        val suggestedApps = listOf(
            AppItem(
                name = "Mech Assemble: Zombie Swarm",
                category = "Action · Role Playing · Roguelike",
                rating = 4.8f,
                sizeMb = 624
            ),
            AppItem(
                name = "MU: Hồng Hỏa Đao",
                category = "Role Playing",
                rating = 4.8f,
                sizeMb = 339
            ),
            AppItem(
                name = "War Inc: Rising",
                category = "Strategy · Tower defense",
                rating = 4.9f,
                sizeMb = 231
            )
        )

        val recommendedApps = listOf(
            AppItem("Suno", "Music & Audio", 4.6f, 120),
            AppItem("Claude by ...", "Productivity", 4.7f, 80),
            AppItem("DramaBox", "Entertainment", 4.5f, 95),
            AppItem("Another app", "Tools", 4.2f, 50)
        )

        return listOf(
            AppRow(
                title = "Sponsored · Suggested for you",
                apps = suggestedApps,
                layoutType = AppRow.LayoutType.VERTICAL_LIST
            ),
            AppRow(
                title = "Recommended for you",
                apps = recommendedApps,
                layoutType = AppRow.LayoutType.HORIZONTAL_STRIP
            )
        )
    }
}

package com.example.newsapplication.presenter.screen

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapplication.R
import com.example.newsapplication.presenter.screen.source.SourcesActivity
import kotlinx.android.synthetic.main.activity_main.*

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            this.resources.getStringArray(R.array.list_category)
        )

        // attach the array adapter with list view
        lvCategory.adapter = adapter

        // list view item click listener
        lvCategory.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedCategory = parent.getItemAtPosition(position) as String
            val intent = Intent(this, SourcesActivity::class.java)
            intent.putExtra(CATEGORY_ID, selectedCategory)
            startActivity(intent)
        }
    }
}
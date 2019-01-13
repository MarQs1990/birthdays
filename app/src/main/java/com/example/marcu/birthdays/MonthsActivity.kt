package com.example.marcu.birthdays

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter
import android.widget.ListView

import kotlinx.android.synthetic.main.activity_months.*

class MonthsActivity : AppCompatActivity() {

    val months: List<String> = listOf("Januar", "Februar", "März", "April","Mai","Juni","Juli","August","September","Oktober","November","Dezember")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_months)
        setSupportActionBar(toolbar)

        var monthsListView = findViewById<ListView>(R.id.monthsListView)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, months)

        monthsListView.adapter = adapter

        fab.setOnClickListener { view ->
            val intent = Intent(this, NewPersonActivity::class.java)
            startActivity(intent)
        }
    }

}

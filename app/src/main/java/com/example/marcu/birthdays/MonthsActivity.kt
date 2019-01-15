package com.example.marcu.birthdays

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter
import android.widget.ListView

import kotlinx.android.synthetic.main.activity_months.*

class MonthsActivity : AppCompatActivity() {

    val months: List<String> = listOf("Januar", "Februar", "März", "April","Mai","Juni","Juli","August","September","Oktober","November","Dezember", "Alle Geburtstage")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_months)
        setSupportActionBar(toolbar)

        val actionbar = supportActionBar
        actionbar!!.setTitle("Monate")

        var monthsListView = findViewById<ListView>(R.id.monthsListView)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, months)

        monthsListView.adapter = adapter

        monthsListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, BirthdaysActivity::class.java)
            intent.putExtra("Month", position + 1)
            startActivity(intent)
        }

        fab.setOnClickListener { view ->
            val intent = Intent(this, NewPersonActivity::class.java)
            startActivity(intent)
        }
    }

}

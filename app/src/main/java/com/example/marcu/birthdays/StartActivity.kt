package com.example.marcu.birthdays

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.ExpandableListView
import kotlinx.android.synthetic.main.activity_birthdays.*
import kotlinx.android.synthetic.main.header.*

class StartActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private lateinit var drawer: DrawerLayout
    lateinit var expandableListView: ExpandableListView
    lateinit var expandableListAdapter: ExpandableListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        fab.setOnClickListener {
            val intent = Intent(this, SavePersonActivity::class.java)
            startActivity(intent)
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        drawer.addDrawerListener(toggle)

        toggle.syncState()

        expandableListView = findViewById(R.id.navigation_menu)

        expandableListView.setGroupIndicator(null)

        setItems()
        setListener()
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        var month: Int = -1
        when(p0.itemId){
            R.id.january -> month = 1
            R.id.february -> month = 2
            R.id.march -> month = 3
            R.id.april -> month = 4
            R.id.may -> month = 5
            R.id.june -> month = 6
            R.id.july -> month = 7
            R.id.august -> month = 8
            R.id.september -> month = 9
            R.id.october -> month = 10
            R.id.november -> month = 11
            R.id.december -> month = 12
            R.id.all -> month = 13
            R.id.next10 -> month = 14
        }

        val intent = Intent(this, BirthdaysActivity::class.java)
        intent.putExtra("Month", month)
        startActivity(intent)
        return true
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }

    private fun setItems(){

        val header = mutableListOf<String>()

        val childMonths = mutableListOf<String>()
        val childNextTen = mutableListOf<String>()
        val childAllBirthdays = mutableListOf<String>()

        val hashMap = mutableMapOf<String, MutableList<String>>()

        header.add(getString(R.string.next_ten_birthdays))
        header.add(getString(R.string.months))
        header.add(getString(R.string.all_birthdays))

        childMonths.add(getString(R.string.january))
        childMonths.add(getString(R.string.february))
        childMonths.add(getString(R.string.march))
        childMonths.add(getString(R.string.april))
        childMonths.add(getString(R.string.may))
        childMonths.add(getString(R.string.june))
        childMonths.add(getString(R.string.july))
        childMonths.add(getString(R.string.august))
        childMonths.add(getString(R.string.september))
        childMonths.add(getString(R.string.october))
        childMonths.add(getString(R.string.november))
        childMonths.add(getString(R.string.december))

        hashMap[header[0]] = childNextTen
        hashMap[header[1]] = childMonths
        hashMap[header[2]] = childAllBirthdays

        expandableListAdapter = ExpandableListAdapter(this, header, hashMap)

        expandableListView.setAdapter(expandableListAdapter)
    }

    private fun setListener(){
    }


}

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

class StartActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        var month: Int = -1
        when(p0.itemId){
            R.id.january -> month = 0
            R.id.february -> month = 1
            R.id.march -> month = 2
            R.id.april -> month = 3
            R.id.may -> month = 4
            R.id.june -> month = 5
            R.id.july -> month = 6
            R.id.august -> month = 7
            R.id.september -> month = 8
            R.id.october -> month = 9
            R.id.november -> month = 10
            R.id.december -> month = 11
            R.id.all -> month = 12
        }

        val intent = Intent(this, BirthdaysActivity::class.java)
        intent.putExtra("Month", month)
        startActivity(intent)
        return true
    }

    private lateinit var drawer: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)


        drawer.addDrawerListener(toggle)

        toggle.syncState()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }


}

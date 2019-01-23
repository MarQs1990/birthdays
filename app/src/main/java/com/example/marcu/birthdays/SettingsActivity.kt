package com.example.marcu.birthdays

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SettingsActivity : AppCompatActivity() {

    //var alarmBeforeBirthday: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val actionbar = supportActionBar
        actionbar!!.title = ""

    }

    private fun setSharedPreferences(){
        val prefs = applicationContext.getSharedPreferences("birthdays preferences", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        editor.putBoolean("first time today", true)
        editor.putInt("alarm before birthday", 10) //TODO In Settings packen und da aufrufen
    }
}

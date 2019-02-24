package com.example.marcu.birthdays.activities

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.marcu.birthdays.R

class SettingsActivity : AppCompatActivity() {

    //var alarmBeforeBirthday: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initiateToolbar()
    }

    @SuppressLint("RestrictedApi")
    private fun initiateToolbar(){
        val actionBar = supportActionBar
        actionBar!!.setDefaultDisplayHomeAsUpEnabled(true)
        actionBar.title = getString(R.string.toolbar_settings)
    }

    @SuppressLint("CommitPrefEdits")
    private fun setSharedPreferences(){
        val prefs = applicationContext.getSharedPreferences("birthdays preferences", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        editor.putBoolean("first time today", true)
        editor.putInt("alarm before birthday", 10) //TODO In Settings packen und da aufrufen
    }
}

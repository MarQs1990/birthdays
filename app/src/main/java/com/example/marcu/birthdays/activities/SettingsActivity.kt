package com.example.marcu.birthdays.activities

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.text.Editable
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Switch
import com.example.marcu.birthdays.core.hourReminder
import com.example.marcu.birthdays.core.minuteReminder
import com.example.marcu.birthdays.gui.AlarmTimePicker
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var alarmIsActive: Switch
    private lateinit var alarmBeforeBirthdayIsActive: Switch
    private lateinit var daysReminderBeforeBirthday: EditText
    lateinit var reminderTimeText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.marcu.birthdays.R.layout.activity_settings)

        alarmIsActive = findViewById(com.example.marcu.birthdays.R.id.birthdayAlarmSwitch)
        alarmBeforeBirthdayIsActive = findViewById(com.example.marcu.birthdays.R.id.BirthdayReminderSwitch)
        daysReminderBeforeBirthday = findViewById(com.example.marcu.birthdays.R.id.reminderDays)
        reminderTimeText = findViewById(com.example.marcu.birthdays.R.id.reminderTimeText)
        reminderTimeText.text = Editable.Factory.getInstance().newEditable("$hourReminder : $minuteReminder")
        reminderTimeText.inputType = InputType.TYPE_NULL
        reminderTimeText.setOnClickListener {
            View.OnClickListener{
                val timePickerFragment = AlarmTimePicker()
                timePickerFragment.show(supportFragmentManager, "TimePicker")
            }
        }
        reminderTimeText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val timePickerFragment = AlarmTimePicker()
                timePickerFragment.show(supportFragmentManager, "TimePicker")
            }
        };
        initiateToolbar()
    }

    @SuppressLint("RestrictedApi")
    private fun initiateToolbar(){
        val actionBar = supportActionBar
        actionBar!!.setDefaultDisplayHomeAsUpEnabled(true)
        actionBar.title = getString(com.example.marcu.birthdays.R.string.toolbar_settings)
    }

    @SuppressLint("CommitPrefEdits")
    private fun setSharedPreferences(){
        val prefs = applicationContext.getSharedPreferences("birthdays preferences", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        editor.putBoolean("first time today", true)
        editor.putInt("alarm before birthday", 10) //TODO In Settings packen und da aufrufen
    }
}

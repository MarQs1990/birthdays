package com.example.marcu.birthdays.activities

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.InputType
import android.view.MotionEvent
import android.widget.EditText
import android.widget.Switch
import com.example.marcu.birthdays.R
import com.example.marcu.birthdays.core.daysReminder
import com.example.marcu.birthdays.core.hourReminder
import com.example.marcu.birthdays.core.minuteReminder
import com.example.marcu.birthdays.fragments.AlarmTimePickerFragment
import com.example.marcu.birthdays.fragments.ReminderDaysPickerFragment

class SettingsActivity : AppCompatActivity(), AlarmTimePickerFragment.TimePickerListener, ReminderDaysPickerFragment.DayPickerListener {

    /*
    get the Value from the NumberPickerFragment,
    that open if you touch the two EdiTexts for the Alarm
     */
    override fun applyDays(days: String) {
        daysReminderBeforeBirthday.text = Editable.Factory.getInstance().newEditable(days)
    }

    override fun applyTime(time: String) {
        reminderTimeText.text = Editable.Factory.getInstance().newEditable(time)
    }

    private lateinit var toolbar: Toolbar
    private lateinit var alarmIsActive: Switch
    private lateinit var alarmBeforeBirthdayIsActive: Switch
    private lateinit var daysReminderBeforeBirthday: EditText
    private lateinit var reminderTimeText: EditText

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.marcu.birthdays.R.layout.activity_settings)

        alarmIsActive = findViewById(com.example.marcu.birthdays.R.id.birthdayAlarmSwitch)
        alarmBeforeBirthdayIsActive = findViewById(com.example.marcu.birthdays.R.id.BirthdayReminderSwitch)

        daysReminderBeforeBirthday = findViewById(com.example.marcu.birthdays.R.id.reminderDays)
        daysReminderBeforeBirthday.text = Editable.Factory.getInstance().newEditable(daysReminder.toString())
        daysReminderBeforeBirthday.inputType = InputType.TYPE_NULL
        daysReminderBeforeBirthday.setOnTouchListener { _, event ->
            if(event.action == MotionEvent.ACTION_UP){
                showDayPickerFragment()
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }

        reminderTimeText = findViewById(com.example.marcu.birthdays.R.id.reminderTimeText)
        reminderTimeText.text = Editable.Factory.getInstance().newEditable("$hourReminder : $minuteReminder")
        reminderTimeText.inputType = InputType.TYPE_NULL
        reminderTimeText.setOnTouchListener { _, event ->
            if(event.action == MotionEvent.ACTION_UP){
                showTimePickerFragment()
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }

        initiateToolbar()
    }

    @SuppressLint("RestrictedApi")
    private fun initiateToolbar(){
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(com.example.marcu.birthdays.R.string.toolbar_settings)
        toolbar.setBackgroundColor(getColor(R.color.colorPrimary))
        toolbar.setTitleTextColor(Color.WHITE)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun showTimePickerFragment(){
        AlarmTimePickerFragment().show(supportFragmentManager, "fragment_pick_time")
    }

    private fun showDayPickerFragment(){
        ReminderDaysPickerFragment().show(supportFragmentManager, "fragment_pick_days")
    }

    //TODO Muss mal komplettiert werden
    @SuppressLint("CommitPrefEdits")
    private fun setSharedPreferences(){
        val prefs = applicationContext.getSharedPreferences("birthdays preferences", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        editor.putBoolean("first time today", true)
        editor.putInt("alarm before birthday", 10)
    }

}

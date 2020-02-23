package com.example.marcu.birthdays.core.notification

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor


class AlarmData(){
    private val APP_SHARED_PREFS = "RemindMePref"

    private var appSharedPrefs: SharedPreferences? = null
    private var prefsEditor: Editor? = null

    private val reminderStatus = "reminderStatus"
    private val hour = "hour"
    private val min = "min"

    fun LocalData(context: Context) {
        appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE)
        prefsEditor = appSharedPrefs?.edit()
    }

    // Settings Page Set Reminder
    fun getReminderStatus(): Boolean {
        return appSharedPrefs!!.getBoolean(reminderStatus, false)
    }

    fun setReminderStatus(status: Boolean) {
        prefsEditor!!.putBoolean(reminderStatus, status)
        prefsEditor!!.commit()
    }

    // Settings Page Reminder Time (Hour)
    fun get_hour(): Int {
        return appSharedPrefs!!.getInt(hour, 20)
    }

    fun set_hour(h: Int) {
        prefsEditor!!.putInt(hour, h)
        prefsEditor!!.commit()
    }

    // Settings Page Reminder Time (Minutes)
    fun get_min(): Int {
        return appSharedPrefs!!.getInt(min, 0)
    }

    fun set_min(m: Int) {
        prefsEditor!!.putInt(min, m)
        prefsEditor!!.commit()
    }

    fun reset() {
        prefsEditor!!.clear()
        prefsEditor!!.commit()
    }
}
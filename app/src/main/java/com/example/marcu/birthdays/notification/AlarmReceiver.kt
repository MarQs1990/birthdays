package com.example.marcu.birthdays.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.util.Log
import com.example.marcu.birthdays.R
import com.example.marcu.birthdays.activities.BirthdaysActivity

class AlarmReceiver : BroadcastReceiver() {

    private val tag = "AlarmReceiver"

    override fun onReceive(context: Context, intent: Intent) {

        val title = Resources.getSystem().getString(R.string.notification_title)
        val content = Resources.getSystem().getString(R.string.notification_content)

        NotificationScheduler.showNotification(context, BirthdaysActivity::class.java, title, content)
    }
}

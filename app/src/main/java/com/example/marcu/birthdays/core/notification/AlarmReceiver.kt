package com.example.marcu.birthdays.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import com.example.marcu.birthdays.R
import com.example.marcu.birthdays.gui.activities.BirthdaysActivity

class AlarmReceiver : BroadcastReceiver() {

    private val tag = "AlarmReceiver"

    override fun onReceive(context: Context, intent: Intent) {

        val title = Resources.getSystem().getString(R.string.notification_title)
        val content = Resources.getSystem().getString(R.string.notification_content)

        NotificationScheduler.showNotification(context, BirthdaysActivity::class.java, title, content)
    }
}

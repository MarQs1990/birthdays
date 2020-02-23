package com.example.marcu.birthdays.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.marcu.birthdays.R

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == (Intent.ACTION_LOCKED_BOOT_COMPLETED)) {
            Log.d("Boot receiver", "on receive: BOOT_COMPLETED")
            AlarmService.setAlarm(context, 7, 0)
        }

        Log.d("ReminderReceiver", "on receive")

        val title = context.resources.getString(R.string.notification_title)
        val content = context.resources.getString(R.string.notification_content)

        NotificationService.showNotification(context, title, content)
    }
}

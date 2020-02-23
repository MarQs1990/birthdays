package com.example.marcu.birthdays.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.util.Log
import com.example.marcu.birthdays.R
import java.util.*

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == (Intent.ACTION_LOCKED_BOOT_COMPLETED)) {
            Log.d("Boot receiver", "on receive: BOOT_COMPLETED")
            AlarmService.setAlarm(context, 7, 0)
        }

        val scheduledCalendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 7)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if (Calendar.getInstance().after(scheduledCalendar)) {
            val title = Resources.getSystem().getString(R.string.notification_title)
            val content = Resources.getSystem().getString(R.string.notification_content)

            NotificationService.showNotification(context, title, content)
        }
    }
}
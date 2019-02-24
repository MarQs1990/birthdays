package com.example.marcu.birthdays.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.marcu.birthdays.activities.BirthdaysActivity

class AlarmReceiver : BroadcastReceiver() {

    private val tag = "AlarmReceiver"

    override fun onReceive(context: Context?, intent: Intent) {

        if (intent.action != null && context != null) {
            if (intent.action!!.equals(Intent.ACTION_BOOT_COMPLETED, ignoreCase = true)) {
                // Set the alarm here.
                Log.d(tag, "onReceive: BOOT_COMPLETED")
                NotificationScheduler.setAlarm(context)
                return
            }
        }

        Log.d(tag, "onReceive: ")
        //Trigger the notification
        NotificationScheduler.showNotification(
            context!!,
            BirthdaysActivity::class.java,
            "Geburtstagserinnerung",
            "Es stehen demnächst Geburtstage an"
        )
    }
}

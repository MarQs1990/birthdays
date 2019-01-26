package com.example.marcu.birthdays

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {

    private val tag = "AlarmReceiver"

    override fun onReceive(context: Context?, intent: Intent) {

        if (intent.action != null && context != null) {
            if (intent.action!!.equals(Intent.ACTION_BOOT_COMPLETED, ignoreCase = true)) {
                // Set the alarm here.
                Log.d(tag, "onReceive: BOOT_COMPLETED")
                NotificationScheduler.setReminder(
                    context,
                    AlarmReceiver::class.java,
                    7,
                    0
                )
                return
            }
        }

        Log.d(tag, "onReceive: ")

        //Trigger the notification
        //TODO Bedingung für das Zeigen der Notification implementieren
        NotificationScheduler.showNotification(
            context!!, BirthdaysActivity::class.java, "Geburtstagserinnerung", "Es stehen demnächst Geburtstage an"
        )

    }
}

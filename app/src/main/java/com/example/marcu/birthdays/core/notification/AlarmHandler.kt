package com.example.marcu.birthdays.core.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.example.marcu.birthdays.core.DAILY_REMINDER_REQUEST_CODE
import java.util.*

object AlarmHandler {
    private var alarmManager: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent
    fun setAlarm(context: Context, hour: Int, min: Int) {
        enableBootReceiver(context)
        val scheduledCalendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, min)
            set(Calendar.SECOND, 0)
        }

        val startUpTime = scheduledCalendar.timeInMillis

        val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context,
                DAILY_REMINDER_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            startUpTime,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
    }

    fun cancelAlarm(context: Context) {
        disableBootReceiver(context)

        alarmManager?.cancel(alarmIntent)
    }

    private fun enableBootReceiver(context: Context) {
        val receiver = ComponentName(context, BootReceiver::class.java)

        context.packageManager.setComponentEnabledSetting(
            receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
        )
    }

    private fun disableBootReceiver(context: Context) {
        val receiver = ComponentName(context, BootReceiver::class.java)

        context.packageManager.setComponentEnabledSetting(
            receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
        )
    }
}
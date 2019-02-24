package com.example.marcu.birthdays.notification

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder

import java.util.Calendar

import android.content.Context.ALARM_SERVICE
import com.example.marcu.birthdays.core.CHANNEL_ID
import com.example.marcu.birthdays.core.DAILY_REMINDER_REQUEST_CODE
import com.example.marcu.birthdays.R
import com.example.marcu.birthdays.core.birthdayToday


object NotificationScheduler {

    fun setReminder(context: Context, cls: Class<*>, hour: Int = 7, min: Int = 0) {
        val calendar = Calendar.getInstance()

        val setcalendar = Calendar.getInstance()
        setcalendar.set(Calendar.HOUR_OF_DAY, hour)
        setcalendar.set(Calendar.MINUTE, min)
        setcalendar.set(Calendar.SECOND, 0)

        // cancel already scheduled reminders
        cancelReminder(context, cls)

        if (setcalendar.before(calendar)) setcalendar.add(Calendar.DATE, 1)

        // Enable a receiver

        val receiver = ComponentName(context, cls)
        val pm = context.packageManager

        pm.setComponentEnabledSetting(
            receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
        )


        val intent1 = Intent(context, cls)
        val pendingIntent =
            PendingIntent.getBroadcast(context,
                DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT)
        val am = context.getSystemService(ALARM_SERVICE) as AlarmManager
        am.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            setcalendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun cancelReminder(context: Context, cls: Class<*>) {
        // Disable a receiver

        val receiver = ComponentName(context, cls)
        val pm = context.packageManager

        pm.setComponentEnabledSetting(
            receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
        )

        val intent1 = Intent(context, cls)
        val pendingIntent =
            PendingIntent.getBroadcast(context,
                DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT)
        val am = context.getSystemService(ALARM_SERVICE) as AlarmManager
        am.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    fun showNotification(context: Context, cls: Class<*>, title: String, content: String) {

        if (birthdayToday){
            val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val notificationIntent = Intent(context, cls)
            notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addParentStack(cls)
            stackBuilder.addNextIntent(notificationIntent)

            val pendingIntent =
                stackBuilder.getPendingIntent(DAILY_REMINDER_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT)

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)

            val notification =
                builder.setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setSound(alarmSound)
                    .setSmallIcon(R.drawable.notification_small)
                    .setContentIntent(pendingIntent).build()

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, notification)
        }


    }

}
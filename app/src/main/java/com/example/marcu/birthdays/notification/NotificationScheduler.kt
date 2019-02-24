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

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    fun setAlarm(context: Context?){
        if (context != null) {
            alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(context, 0, intent, 0)
            }

            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 7)
                set(Calendar.MINUTE, 0)
            }

            alarmMgr?.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                alarmIntent
            )
        }
    }

    fun cancelAlarm(){
        alarmMgr?.cancel(alarmIntent)
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

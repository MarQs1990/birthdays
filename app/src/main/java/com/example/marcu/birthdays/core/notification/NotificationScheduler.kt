package com.example.marcu.birthdays.core.notification

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
import com.example.marcu.birthdays.birthdays.BirthdaysDBHandler
import java.time.LocalDateTime


object NotificationScheduler {

    fun setAlarm(context: Context, cls: Class<*>, hour: Int, min: Int) {
        val calendar = Calendar.getInstance()
        val setCalendar = Calendar.getInstance()
        setCalendar.set(Calendar.HOUR_OF_DAY, hour)
        setCalendar.set(Calendar.MINUTE, min)
        setCalendar.set(Calendar.SECOND, 0)

        cancelAlarm(context, cls)

        if (setCalendar.before(calendar)) {
            setCalendar.add(Calendar.DATE, 1)
        }

        val receiver = ComponentName(context, cls)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
        )

        val intent1 = Intent(context, cls)
        val pendingIntent = PendingIntent.getBroadcast(
            context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val am = context.getSystemService(ALARM_SERVICE) as AlarmManager
        am.setInexactRepeating(
            AlarmManager.RTC_WAKEUP, setCalendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent
        )
    }

    fun cancelAlarm(context: Context, cls: Class<*>){
        val receiver = ComponentName(context, cls)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP)

        val intent1 = Intent(context, cls)
        val pendingIntent = PendingIntent.getBroadcast(context,
            DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT)

        val am = context.getSystemService(ALARM_SERVICE) as AlarmManager
        am.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    fun showNotification(context: Context, cls: Class<*>, title: String, content: String) {
        if (birthdaysToday(context)){
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

    private fun birthdaysToday(context: Context): Boolean{

        val dbHandler =
            BirthdaysDBHandler(context)

        val birthdays = dbHandler.getAllBirthdays()

        var isBirthdayToday = false

        val today = LocalDateTime.now()
        for (birthday in birthdays){
            isBirthdayToday = today.dayOfMonth == birthday.birthday.dayOfMonth
                    && today.monthValue == birthday.birthday.monthValue
        }

        return isBirthdayToday
    }

}

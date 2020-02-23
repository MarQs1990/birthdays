package com.example.marcu.birthdays.core.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.TaskStackBuilder
import com.example.marcu.birthdays.R
import com.example.marcu.birthdays.birthdays.BirthdaysDBHandler
import com.example.marcu.birthdays.core.DAILY_REMINDER_REQUEST_CODE
import com.example.marcu.birthdays.gui.activities.BirthdaysActivity
import java.time.LocalDateTime

object NotificationService {
    private const val CHANNEL_ID = "BIRTHDAYALARM"
    private const val CHANNEL_NAME = "Birthday Alarm"
    private const val importance = NotificationManager.IMPORTANCE_DEFAULT
    private val activityForIntent = BirthdaysActivity::class.java

    fun showNotification(context: Context, title: String, content: String) {
        createNotificationChannel(context)
        val notification = createNotification(context, title, content)
        if (birthdaysToday(context)) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, notification)
        }
    }


    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val descriptionText = context.resources.getString(R.string.channel_description)
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = descriptionText
            }
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.lightColor = Color.WHITE
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(context: Context, title: String, content: String): Notification {
        val pendingIntent = createPendingIntent(context)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        return builder
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setSmallIcon(R.drawable.notification_small)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun birthdaysToday(context: Context): Boolean {
        val dbHandler = BirthdaysDBHandler(context)

        val birthdays = dbHandler.getAllBirthdays()

        var isBirthdayToday = false

        val today = LocalDateTime.now()
        for (birthday in birthdays) {
            isBirthdayToday =
                today.dayOfMonth == birthday.birthday.dayOfMonth && today.monthValue == birthday.birthday.monthValue
        }

        return isBirthdayToday
    }

    private fun createPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, activityForIntent)
        return PendingIntent.getActivity(context, DAILY_REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}

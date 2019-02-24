package com.example.marcu.birthdays.notification

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver: BroadcastReceiver(){
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        NotificationScheduler.setAlarm(context)
    }
}
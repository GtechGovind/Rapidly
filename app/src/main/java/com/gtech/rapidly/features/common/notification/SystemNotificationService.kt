package com.gtech.rapidly.features.common.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gtech.rapidly.R
import com.gtech.rapidly.app.RapidlyApp
import com.gtech.rapidly.features.activity.MainActivity
import com.gtech.rapidly.utils.error

object SystemNotificationService {

    private const val CHANNEL_ID = "TEJ_CHANNEL_ID"
    private const val CHANNEL_NAME = "TEJ_CHANNEL"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = RapidlyApp.instance.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    fun notify(title: String, message: String) {
        try {

            val context = RapidlyApp.instance
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE)
            val notification = NotificationCompat.Builder(RapidlyApp.instance, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
            with(NotificationManagerCompat.from(RapidlyApp.instance)) {
                notify(1, notification)
            }
        } catch (e: Exception) {
            error(e)
        }
    }

}
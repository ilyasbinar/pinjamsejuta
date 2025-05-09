package com.example.pinjamsejuta.network

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.pinjamsejuta.R
import com.google.android.datatransport.Priority
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.lang.Exception

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("FCM", "From: ${remoteMessage.from}")

        // Jika pesan berisi data
        remoteMessage.data.isNotEmpty().let {
            Log.d("FCM", "Message data payload: ${remoteMessage.data}")
        }

        // Jika pesan berisi notifikasi
        remoteMessage.notification?.let {
            Log.d("FCM", "Message Notification Body: ${it.body}")
        }
        showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Refreshed token: $token")
        // Kirim token ke server
    }

    override fun onSendError(msgId: String, exception: Exception) {
        super.onSendError(msgId, exception)
        Log.d("FCM", "Error : $exception")
    }

    private fun showNotification(title: String?, message: String?) {
        val channelId = "default_channel_id"
        val channelName = "Default Channel"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create channel for Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH // 🔥 HEADS-UP for Android 8+
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_dashboard_black_24dp)
            .setContentTitle(title ?: "Notifikasi")
            .setContentText(message ?: "")
            .setPriority(NotificationCompat.PRIORITY_HIGH) // 🔥 HEADS-UP for below Android 8
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }
}

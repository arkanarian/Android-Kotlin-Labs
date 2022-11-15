package com.example.lab2

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.lab2.util.NotificationUtil
import com.example.lab2.util.NotificationUtil.Companion.createNotificationChannel

class MainService : Service() {

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
    }

    // когда пишет startService()
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("NotificationUtil", "----------- Service Started -----------")
//        val startIntent = Intent(this, TimerStart::class.java)
//        startIntent.action = AppConstants.ACTION_START
//        val startPendingIntent = PendingIntent.getBroadcast(
//            this,
//            0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
//        val nBuilder = NotificationUtil.getBasicNotificationBuilder(
//            this,
//            App.CHANNEL_ID,
//            true
//        )
//
//        nBuilder.setContentTitle("Timer is paused.")
//            .setContentText("Resume?")
//            .setContentIntent(
//                NotificationUtil.getPendingIntentWithStack(
//                    this,
//                    TimerStart::class.java
//                )
//            )
//            .setOngoing(true)

        val notificationIntent = Intent(this, TimerStart::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notificiation = NotificationCompat.Builder(this, App.CHANNEL_ID)
            .setContentTitle("Main Service")
            .setContentText("Hellllo")
            .setSmallIcon(R.drawable.ic_timer)
//            .setContentIntent(pendingIntent)
            .build()
//        val nManager =
//            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        nManager.createNotificationChannel(
//            App.CHANNEL_ID,
//            App.CHANNEL_NAME, true)
//        nManager.notify(NotificationUtil.TIMER_ID, nBuilder.build())

//        return super.onStartCommand(intent, flags, startId)

        startForeground(1, notificiation)
        return START_NOT_STICKY
    }

    // service is destroying
    override fun onDestroy() {
        Log.d("NotificationUtil", "----------- Service Destroyed -----------")
        super.onDestroy()
    }
}
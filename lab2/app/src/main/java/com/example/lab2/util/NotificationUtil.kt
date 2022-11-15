package com.example.lab2.util

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.lab2.*
import java.text.SimpleDateFormat
import java.util.*

class NotificationUtil {
//    override fun onBind(p0: Intent?): IBinder? = null
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        Log.d("NotificationUtil", "----------- Service Started -----------")
//        return super.onStartCommand(intent, flags, startId)
//    }
//
//    // service is destroying
//    override fun onDestroy() {
//        Log.d("NotificationUtil", "----------- Service Destroyed -----------")
//        super.onDestroy()
//    }

    companion object {
        private const val CHANNEL_ID_TIMER = "menu_timer"
        private const val CHANNEL_NAME_TIMER = "Timer App Timer"
        private const val TIMER_ID = 0

        fun startNewPhase(context: Context) {
            val secondsRemaining = PrefUtil.getTimerLength(context).toLong()
            val wakeUpTime = TimerStart.setAlarm(context, TimerStart.nowSeconds, secondsRemaining)
            Log.d("ACTION_START", "---------- executed ----------")
            Log.d("secondsRemaining", "----------" + secondsRemaining + "----------")
            PrefUtil.printPhasesInfo()

            PrefUtil.setTimerState(TimerStart.TimerState.Running, context)
            PrefUtil.setSecondsRemaining(secondsRemaining, context)
            NotificationUtil.showTimerRunning(context, wakeUpTime)
        }

        fun showTimerExpired(context: Context) {
            val startIntent = Intent(context, TimerNotificationActionReceiver::class.java)
            startIntent.action = AppConstants.ACTION_START
            val startPendingIntent = PendingIntent.getBroadcast(
                context,
                0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )

            val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)
            nBuilder.setContentTitle("Timer Expired!")
                .setContentText("Start again?")
                .setContentIntent(getPendingIntentWithStack(context, TimerStart::class.java))
                // елси хотим еще раз запустить таймер нажимаем на кнопку
                .addAction(R.drawable.ic_play, "Start", startPendingIntent)

            val nManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, true)

            nManager.notify(TIMER_ID, nBuilder.build())
        }

        fun showTimerRunning(context: Context, wakeUpTime: Long) {
            val stopIntent = Intent(context, TimerNotificationActionReceiver::class.java)
            stopIntent.action = AppConstants.ACTION_STOP
            val stopPendingIntent = PendingIntent.getBroadcast(
                context,
                0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )

            val pauseIntent = Intent(context, TimerNotificationActionReceiver::class.java)
            pauseIntent.action = AppConstants.ACTION_PAUSE
            val pausePendingIntent = PendingIntent.getBroadcast(
                context,
                0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )

            // import from java??? мб из kotlin
            val df = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)

            val seconds = PrefUtil.getSecondsRemaining(context)
            val state = PrefUtil.getPhaseState()
            val phaseName = PrefUtil.getCurrentPhaseName()
            val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)
            nBuilder.setContentTitle("${phaseName} (${state})")
//                .setContentText("End: ${df.format(Date(wakeUpTime))}")
                .setContentText("Sec: ${seconds}")
                // TODO: по клику на notification открывается TimerStart activity
                // TODO: (но открывается криво)
//                .setContentIntent(getPendingIntentWithStack(context, TimerStart::class.java))
                .setOngoing(true) // dissmissing notification only from code
                .addAction(R.drawable.ic_stop, "Stop", stopPendingIntent)
                .addAction(R.drawable.ic_pause, "Pause", pausePendingIntent)

            val nManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, true)

            nManager.notify(TIMER_ID, nBuilder.build())
        }

        fun showTimerPaused(context: Context) {
            val resumeIntent = Intent(context, TimerNotificationActionReceiver::class.java)
            resumeIntent.action = AppConstants.ACTION_RESUME
            val resumePendingIntent = PendingIntent.getBroadcast(
                context,
                0, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )

            val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)
            nBuilder.setContentTitle("Timer is paused.")
                .setContentText("Resume?")
                .setContentIntent(getPendingIntentWithStack(context, TimerStart::class.java))
                .setOngoing(true)
                .addAction(R.drawable.ic_play, "Resume", resumePendingIntent)

            val nManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, true)

            nManager.notify(TIMER_ID, nBuilder.build())
        }

        fun hideTimerNotification(context: Context) {
            val nManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.cancel(TIMER_ID)
        }

        fun getBasicNotificationBuilder(
            context: Context,
            channelId: String,
            playSound: Boolean
        )
                : NotificationCompat.Builder {
            val notificationSound: Uri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val nBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_timer)
                .setAutoCancel(true) // когда кликаем по notification, таймер заканчивается
                .setDefaults(0)
            if (playSound) nBuilder.setSound(notificationSound)
            return nBuilder
        }

        // нужно когда кликаем на notification, переходим на TimerStart activity,
        // и если кликнем назад, то должна открыться MainActivity (используется стек PendingIntent)
        fun <T> getPendingIntentWithStack(
            context: Context,
            javaClass: Class<T>
        ): PendingIntent {
            val resultIntent = Intent(context, javaClass)
            resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

            // !! androidx или android???? я ипортировал из android
            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addParentStack(javaClass)
            stackBuilder.addNextIntent(resultIntent)

            return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        @TargetApi(26)
        fun NotificationManager.createNotificationChannel(
            channelID: String,
            channelName: String,
            playSound: Boolean
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelImportance = if (playSound) NotificationManager.IMPORTANCE_DEFAULT
                else NotificationManager.IMPORTANCE_LOW
                val nChannel = NotificationChannel(channelID, channelName, channelImportance)
                nChannel.enableLights(true)
                nChannel.lightColor = Color.BLUE
                this.createNotificationChannel(nChannel)
            }
        }


    }
}
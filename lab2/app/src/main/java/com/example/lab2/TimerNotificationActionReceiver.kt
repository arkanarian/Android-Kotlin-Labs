package com.example.lab2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.lab2.util.NotificationUtil
import com.example.lab2.util.PrefUtil

class TimerNotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action){
            AppConstants.ACTION_STOP -> {
                TimerStart.removeAlarm(context)
                PrefUtil.setTimerState(TimerStart.TimerState.Stopped, context)
                PrefUtil.resetCurrentPhase()
                NotificationUtil.hideTimerNotification(context)
            }
            AppConstants.ACTION_PAUSE -> {
                var secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val alarmSetTime = PrefUtil.getAlarmSetTime(context)
                val nowSeconds = TimerStart.nowSeconds

                secondsRemaining -= nowSeconds - alarmSetTime
                PrefUtil.setSecondsRemaining(secondsRemaining, context)

                TimerStart.removeAlarm(context)
                PrefUtil.setTimerState(TimerStart.TimerState.Paused, context)
                NotificationUtil.showTimerPaused(context)
            }
            AppConstants.ACTION_RESUME -> {
                val secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val wakeUpTime = TimerStart.setAlarm(context, TimerStart.nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(TimerStart.TimerState.Running, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
            AppConstants.ACTION_START -> {
                val secondsRemaining = PrefUtil.getTimerLength(context).toLong()
                val wakeUpTime = TimerStart.setAlarm(context, TimerStart.nowSeconds, secondsRemaining)
                Log.d("ACTION_START", "---------- executed ----------")
                Log.d("secondsRemaining", "----------" + secondsRemaining + "----------")
                PrefUtil.printPhasesInfo()

                PrefUtil.setTimerState(TimerStart.TimerState.Running, context)
                PrefUtil.setSecondsRemaining(secondsRemaining, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
        }
    }
}
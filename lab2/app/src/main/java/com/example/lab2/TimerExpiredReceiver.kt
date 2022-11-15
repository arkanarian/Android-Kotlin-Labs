package com.example.lab2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.lab2.util.NotificationUtil
import com.example.lab2.util.PrefUtil

class TimerExpiredReceiver : BroadcastReceiver() {

    // called whenever the alarm finishes
    override fun onReceive(context: Context, intent: Intent) {
        PrefUtil.nextTimer()
        // данная функция вызывается после окончания таймера
        if (PrefUtil.isTimerEnd()) {
            Log.d("ACTION_START", "----- timer end -----")
            PrefUtil.resetCurrentPhase()
            // TODO: предлагать заново запустить таймер, но важно синхронизировать все
//            NotificationUtil.showTimerExpired(context)
            PrefUtil.setTimerState(TimerStart.TimerState.Stopped, context)
            PrefUtil.setAlarmSetTime(0, context)
            Log.d("TimerExpiredReceiver End", "-------------- executed --------------")
        }
        if (!PrefUtil.isTimerEnd()) {
            NotificationUtil.startNewPhase(context)
        }
        if (PrefUtil.isTimerEnd()) {
            PrefUtil.setTimerStart()
            PrefUtil.printPhasesInfo()
            NotificationUtil.showTimerExpired(context)
        }
    }
}
package com.example.lab2.util

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.preference.PreferenceManager
import com.example.lab2.Phase
import com.example.lab2.R
import com.example.lab2.TimerStart


class PrefUtil : Service() {
    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("PrefUtil", "----------- Service Started -----------")
        return super.onStartCommand(intent, flags, startId)
    }

    // service is destroying
    override fun onDestroy() {
        Log.d("PrefUtil", "----------- Service Destroyed -----------")
        super.onDestroy()
    }

    companion object {
        private var current_phase: Phase? = null
        private var phase_list: MutableList<Phase> = mutableListOf()
        private var isTimerEnd = false
        private var reps = 0
        private var phaseState = PhaseState.Exercise
        enum class PhaseState{
            Exercise, Rest
        }
//        private var timer_length = current_phase!!.duration

        fun getTimerLength(context: Context): Int {
            if (phaseState == PhaseState.Exercise) return current_phase!!.duration
            else if (phaseState == PhaseState.Rest) return current_phase!!.duration_rest
            else return 0
        }

        fun getIndexCurrentPhase(): Int{
            return phase_list.indexOf(current_phase)
        }

        fun isTimerEnd(): Boolean {
            return isTimerEnd
        }

        fun setTimerStart(){
            isTimerEnd = false
        }

        fun setTimerEnd(){
            isTimerEnd = true
        }

        fun getReps(): Int {
            return reps
        }

        fun resetCurrentPhase() {
            current_phase = phase_list[0]
            phaseState = PhaseState.Exercise
            reps = 0
        }

        fun initFirstPhase() {
            if(current_phase == null){
                Log.d("init", "--- first phase ---")
                // initialize first phase
                current_phase = phase_list[0]
                phaseState = PhaseState.Exercise
                reps = 0
            }
        }
        fun printPhasesInfo() {
            Log.d("current phase", "-----------" + current_phase + "-----------")
            Log.d("current state", "-----------" + phaseState + "-----------")
        }

        fun nextTimer(context: Context) {
//            Log.d("reps --->", reps.toString())
//            Log.d("phase state", phaseState.toString())
            if (phaseState == PhaseState.Exercise)
            {
                if (reps < current_phase!!.repetitions)
                {
                    reps += 1
                }
                phaseState = PhaseState.Rest
            }
            else if (phaseState == PhaseState.Rest)
            {
                if (reps < current_phase!!.repetitions)
                {
                    phaseState = PhaseState.Exercise
                }
                else {
                    nextPhase()
                }
            }
            activateSound(context)
        }

        fun nextPhase(){
            val index = phase_list.indexOf(current_phase)
            if (index < phase_list.size-1) {
                current_phase = phase_list[index + 1]
                phaseState = PhaseState.Exercise
                reps = 0
            }
            else {
                setTimerEnd()
                phaseState = PhaseState.Exercise
                reps = 0
            }
        }

        fun prevPhase(){
            val index = phase_list.indexOf(current_phase)
            if (index > 0) {
                current_phase = phase_list[index - 1]
                phaseState = PhaseState.Exercise
                reps = 0
            }
            else {
                setTimerEnd()
                phaseState = PhaseState.Exercise
                reps = 0
            }
        }

        val getPhaseState = { phaseState.toString() }
        val getCurrentPhaseName = { current_phase!!.name }

        fun initPhaseList(phase_list: List<Phase>){
            this.phase_list = phase_list.toMutableList()
        }

        fun activateSound(context: Context) {
            val mMediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.siii)
            mMediaPlayer.start()
        }

        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "com.lab2.timer.previous_timer_length_seconds"

        fun getPreviousTimerLengthSeconds(context: Context): Long{
            // если таймер запущен и добавилась новая фаза (длительность таймера изменилась),
            // то таймер не крашнется, доработает до конца
            // а последующие таймеры будут с новым временем
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }

        fun setPreviousTimerLengthSeconds(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }


        private const val TIMER_STATE_ID = "com.lab2.timer.timer_state"

        fun getTimerState(context: Context): TimerStart.TimerState{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return TimerStart.TimerState.values()[ordinal]
        }

        fun setTimerState(state: TimerStart.TimerState, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }


        private const val SECONDS_REMAINING_ID = "com.lab2.timer.seconds_remaining"

        fun getSecondsRemaining(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun setSecondsRemaining(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }


        private const val ALARM_SET_TIME_ID = "com.lab2.timer.backgrounded_time"

        fun getAlarmSetTime(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(ALARM_SET_TIME_ID, 0)
        }

        fun setAlarmSetTime(time: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            editor.apply()
        }
    }
}
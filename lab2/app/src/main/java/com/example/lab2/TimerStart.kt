package com.example.lab2

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab2.adapters.PhaseStartAdapter
import com.example.lab2.util.NotificationUtil
import com.example.lab2.util.PrefUtil
import kotlinx.android.synthetic.main.activity_timer_start.*
import kotlinx.android.synthetic.main.content_timer_start.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class TimerStart : AppCompatActivity() {

    companion object {

        fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining: Long): Long{
            val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            // remember the time at which the alarm was set
            PrefUtil.setAlarmSetTime(nowSeconds, context)
            return wakeUpTime
        }

        fun removeAlarm(context: Context){
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtil.setAlarmSetTime(0, context)
        }

        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 1000
    }
    enum class TimerState{
        Stopped, Paused, Running
    }


    private val phaseDatabase by lazy { AppDatabase.getDatabase(this).phaseDao() }
    private val timerDatabase by lazy { AppDatabase.getDatabase(this).timerDao() }
    private lateinit var adapter: PhaseStartAdapter
    private var timer_inst: Timer? = null
    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 0L
    private var timerState = TimerState.Stopped

    private var secondsRemaining: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Activity State", "------ OnCreate -----")
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_timer_start)

        // recycler view
        setRecyclerView()
        initTimerInfo()
        displayPhasesInfo()
        val job = CoroutineScope(IO).launch {
            val phaseList = phaseDatabase?.getPhasesByTimerIdSimple(timer_inst?.id)
            PrefUtil.initPhaseList(phaseList!!.toMutableList())
            PrefUtil.initFirstPhase()
            updateDoneRepsCurrentPhase()
            highlightCurrentPhase()
            resetRepsAllPhases()
        }
        runBlocking {
            job.join()
        }

        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.timer_start_activity_title)


        fab_start.setOnClickListener{v ->
            startTimer()
            timerState = TimerState.Running
            updateButtons()
            val serviceIntent = Intent(this, MainService::class.java)
            startService(serviceIntent)
        }

        fab_pause.setOnClickListener { v ->
            timer.cancel()
            timerState = TimerState.Paused
            updateButtons()
        }

        fab_stop.setOnClickListener { v ->
            timer.cancel()
            PrefUtil.setTimerEnd()
            onTimerFinished()
        }

        fab_next.setOnClickListener { v ->
            if (timerState == TimerState.Stopped) {
                PrefUtil.nextPhase()
                if (PrefUtil.isTimerEnd()) {
                    PrefUtil.resetCurrentPhase()
                    highlightCurrentPhase()
                }
                updateDoneRepsCurrentPhase()
                setNewTimerLength()

                progress_countdown.progress = 0
                PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
                secondsRemaining = timerLengthSeconds

                if (PrefUtil.isTimerEnd()) {
                    PrefUtil.setTimerStart()
                }
            }
            else if (timerState == TimerState.Running) {
                timer.cancel()
                timerState = TimerState.Stopped
                PrefUtil.nextPhase()
                if (PrefUtil.isTimerEnd()) {
                    PrefUtil.resetCurrentPhase()
                    highlightCurrentPhase()
                }
                updateDoneRepsCurrentPhase()
                setNewTimerLength()

                progress_countdown.progress = 0
                PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
                secondsRemaining = timerLengthSeconds

                if (!PrefUtil.isTimerEnd()) {
                    startTimer()
                }
                if (PrefUtil.isTimerEnd()) {
                    PrefUtil.setTimerStart()
                }
            }
            else if (timerState == TimerState.Paused) {
                timer.cancel()
                timerState = TimerState.Stopped
                PrefUtil.nextPhase()
                if (PrefUtil.isTimerEnd()) {
                    PrefUtil.resetCurrentPhase()
                    highlightCurrentPhase()
                }
                updateDoneRepsCurrentPhase()
                setNewTimerLength()

                progress_countdown.progress = 0
                PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
                secondsRemaining = timerLengthSeconds

                if (PrefUtil.isTimerEnd()) {
                    PrefUtil.setTimerStart()
                }
            }
            updateButtons()
            updateCountdownUI()
        }

        fab_previous.setOnClickListener { v ->
            if (timerState == TimerState.Stopped) {
                PrefUtil.prevPhase()
                if (PrefUtil.isTimerEnd()) {
                    PrefUtil.resetCurrentPhase()
                    highlightCurrentPhase()
                }
                updateDoneRepsCurrentPhase()
                setNewTimerLength()

                progress_countdown.progress = 0
                PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
                secondsRemaining = timerLengthSeconds

                if (PrefUtil.isTimerEnd()) {
                    PrefUtil.setTimerStart()
                }
            }
            else if (timerState == TimerState.Running) {
                timer.cancel()
                timerState = TimerState.Stopped
                PrefUtil.prevPhase()
                if (PrefUtil.isTimerEnd()) {
                    PrefUtil.resetCurrentPhase()
                    highlightCurrentPhase()
                }
                updateDoneRepsCurrentPhase()
                setNewTimerLength()

                progress_countdown.progress = 0
                PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
                secondsRemaining = timerLengthSeconds

                if (!PrefUtil.isTimerEnd()) {
                    startTimer()
                }
                if (PrefUtil.isTimerEnd()) {
                    PrefUtil.setTimerStart()
                }
            }
            else if (timerState == TimerState.Paused) {
                timer.cancel()
                timerState = TimerState.Stopped
                PrefUtil.prevPhase()
                if (PrefUtil.isTimerEnd()) {
                    PrefUtil.resetCurrentPhase()
                    highlightCurrentPhase()
                }
                updateDoneRepsCurrentPhase()
                setNewTimerLength()

                progress_countdown.progress = 0
                PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
                secondsRemaining = timerLengthSeconds

                if (PrefUtil.isTimerEnd()) {
                    PrefUtil.setTimerStart()
                }
            }
            updateButtons()
            updateCountdownUI()
        }

        fab_exit.setOnClickListener { v->
            val serviceIntent = Intent(this, MainService::class.java)
            stopService(serviceIntent)
//            startService(serviceIntent)
//            stopService(serviceNotifyIntent)
            finish()
        }

        // TODO: редактирование фазы проверки крашатся
        // TODO: preferences - изменение размера шрифта и языка
        // TODO: service
    }


    override fun onResume() {
        Log.d("Activity State", "------ OnResume -----")
        super.onResume()

        initTimer()
        PrefUtil.printPhasesInfo()

        removeAlarm(this)
        // hide notification
        NotificationUtil.hideTimerNotification(this)
    }

    override fun onPause() {
        Log.d("Activity State", "------ OnPause -----")
        super.onPause()

        if (timerState == TimerState.Running){
            timer.cancel()
            val wakeUpTime = setAlarm(this, nowSeconds, secondsRemaining)
            // show notification
            val serviceIntent = Intent(this, MainService::class.java)

//        startForeground(servicePrefIntent)
            startService(serviceIntent)
//            startForegroundService(servicePrefIntent)
//            serviceNotifyIntent = Intent(this, NotificationUtil::class.java)
//        startForeground(serviceNotifyIntent)
//            startForegroundService(serviceNotifyIntent)
            NotificationUtil.showTimerRunning(this, wakeUpTime)
        }
        else if (timerState == TimerState.Paused){
            // show notification
            NotificationUtil.showTimerPaused(this)
        }

        Log.d("secondsRemaining", "-------------" + secondsRemaining + "-------------")
        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this)
        PrefUtil.setSecondsRemaining(secondsRemaining, this)
        PrefUtil.setTimerState(timerState, this)
    }

    override fun onDestroy() {
        Log.d("Activity State", "------ OnDestroy -----")
        super.onDestroy()
    }

    private fun initTimer(){
        timerState = PrefUtil.getTimerState(this)
        //we don't want to change the length of the timer which is already running
        //if the length was changed in settings while it was backgrounded
        if (timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if (timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtil.getSecondsRemaining(this)
        else
            timerLengthSeconds // full timer length

        //change secondsRemaining according to where the background timer stopped
        val alarmSetTime = PrefUtil.getAlarmSetTime(this)
        if (alarmSetTime > 0)
            secondsRemaining -= nowSeconds - alarmSetTime

        if (secondsRemaining <= 0)
            onTimerFinished()
        // начинаем таймера с того же места, где вышли
        else if (timerState == TimerState.Running) {
            Log.d("start time", "---- here ----")
            startTimer()
        }

        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished(){
        timerState = TimerState.Stopped

        //set the length of the timer to be the one set in SettingsActivity
        //if the length was changed when the timer was running
        PrefUtil.nextTimer(this)
        if (PrefUtil.isTimerEnd()) {
            Log.d("end", "----- timer end ----")
            PrefUtil.resetCurrentPhase()
            highlightCurrentPhase()
        }
        updateDoneRepsCurrentPhase()
        setNewTimerLength()

        progress_countdown.progress = 0
        PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
        secondsRemaining = timerLengthSeconds

        if (!PrefUtil.isTimerEnd()) {
            startTimer()
        }
        if (PrefUtil.isTimerEnd()) {
            PrefUtil.setTimerStart()
        }
        updateButtons()
        updateCountdownUI()
    }

    private fun highlightCurrentPhase(){
//        val phaseList = adapter.currentList.toMutableList()
        adapter.selectedIndex = PrefUtil.getIndexCurrentPhase()
        adapter.notifyDataSetChanged()
    }

    private fun updateDoneRepsCurrentPhase(){
        Log.d("reps done", PrefUtil.getReps().toString())
        adapter.selectedIndex = PrefUtil.getIndexCurrentPhase()
        adapter.reps_done = PrefUtil.getReps()
        currentStatePhase.setText(PrefUtil.getPhaseState())
        adapter.notifyDataSetChanged()
        Log.d("selected index ---->", adapter.selectedIndex.toString())
        phases_start_list.smoothScrollToPosition(adapter.selectedIndex)
    }

    private fun resetRepsAllPhases() {
        Log.d("item count", adapter.currentList.size.toString())
        for (i in 0 until adapter.currentList.size)
        {
            Log.d("HELLLLO0", "---")
            adapter.selectedIndex = i
            adapter.reps_done = i
            adapter.notifyDataSetChanged()
        }
    }

    private fun startTimer(){
        timerState = TimerState.Running
        highlightCurrentPhase()
        updateDoneRepsCurrentPhase()

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength(){
        val lengthInSeconds = PrefUtil.getTimerLength(this)
        timerLengthSeconds = (lengthInSeconds.toLong())
        progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this)
        progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun updateCountdownUI(){
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        textView_countdown.text = "$minutesUntilFinished:${
            if (secondsStr.length == 2) secondsStr
            else "0" + secondsStr}"
        progress_countdown.progress = (timerLengthSeconds - secondsRemaining).toInt()
    }

    private fun updateButtons() {
        when (timerState) {
            TimerState.Running -> {
                fab_start.isEnabled = false
                fab_pause.isEnabled = true
                fab_stop.isEnabled = true
            }
            TimerState.Stopped -> {
                fab_start.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = false
            }
            TimerState.Paused -> {
                fab_start.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = true
            }
        }
    }


    private fun setRecyclerView()
    {
        // устанавливаем для списка адаптер
        val recyclerView = phases_start_list
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        adapter = PhaseStartAdapter(this)

        adapter.setItemListener(object : RecyclerClickListenerPhaseStart {
            // Tap the phase to edit the phase.
            override fun onItemClick(position: Int) {

            }
        })

        recyclerView.adapter = adapter
    }

    private fun displayPhasesInfo()
    {
        lifecycleScope.launch {
            phaseDatabase?.getPhasesByTimerId(timer_inst?.id)?.collect { phaseList ->
                if (phaseList.isNotEmpty()) {
                    adapter.submitList(phaseList)
                    // phases_list.smoothScrollToPosition(localPhaseList.size-1)
                }
            }
        }
    }

    private fun initTimerInfo()
    {
        val intent = getIntent()
        val id = intent.getIntExtra("timer_id", -1)
        val title = intent.getStringExtra("timer_title").toString()
        val color = intent.getStringExtra("timer_color").toString()
        val duration = intent.getIntExtra("timer_duration", -1)
        this.timer_inst = Timer(title, color, duration, id)
//        tvTitle.text = timer_inst?.title
//        etTitle.setText(timer_inst?.title)


        // TODO: настроить смену цвета кнопок (походу ооочень запарно)
//        val line_1: IntArray = intArrayOf(android.R.attr.state_enabled)
//        val line_2: IntArray = intArrayOf(-android.R.attr.state_enabled)
//        val line_3: IntArray = intArrayOf(-android.R.attr.state_checked)
//        val line_4: IntArray = intArrayOf(android.R.attr.state_checked)
//
//        val states: Array<IntArray> = arrayOf(line_1, line_2, line_3, line_4)
//        fab_start.setBackgroundTintList("#D02727")
//        fab_start.setBackgroundTintList(this.getResources().getColorStateList(R.color.green_fab))
    }
}
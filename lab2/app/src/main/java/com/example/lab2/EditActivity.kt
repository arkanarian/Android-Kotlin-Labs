package com.example.lab2

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.phase_adapter.*
import kotlinx.coroutines.launch


class EditActivity : AppCompatActivity() {

    private val phaseDatabase by lazy { AppDatabase.getDatabase(this).phaseDao() }
    private val timerDatabase by lazy { AppDatabase.getDatabase(this).timerDao() }
    private lateinit var adapter: PhaseAdapter
    private var timer: Timer? = null
    private var localPhaseList: MutableList<Phase> = mutableListOf()
    private var todeletePhaseList: MutableList<Phase> = mutableListOf()
    private var localTimerTitle: String = ""
    private var isFieldsCorrect = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setRecyclerView()
        initTimerInfo()
        displayPhasesInfo()

        lifecycleScope.launch {
            phaseDatabase?.getPhasesByTimerId(timer?.id)?.collect { phaseList ->
                localPhaseList = phaseList.toMutableList()
                if (localPhaseList.isNotEmpty()) {
                    adapter.submitList(localPhaseList)
                    phases_list.smoothScrollToPosition(localPhaseList.size-1)
                }
            }
        }

        btnPhaseNew.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                createUnsavedPhase()
//                displayPhasesInfo()
                displayPhasesInfo()
                phases_list.smoothScrollToPosition(localPhaseList.size-1)
            }
        })
        btnTimerSave.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                saveTimerInfo()
//                displayPhasesInfo()
                displayPhasesInfo()
                displayTimerInfo()
            }
        })

    }


    private fun setRecyclerView()
    {
        // устанавливаем для списка адаптер
        val recyclerView = findViewById<RecyclerView>(R.id.phases_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        adapter = PhaseAdapter()

        adapter.setItemListener(object : RecyclerClickListenerPhase {

            // Tap the btnDelete to delete the note.
            override fun onItemRemoveClick(position: Int) {
                val phaseList = adapter.currentList.toMutableList()
                val name = phaseList[position].name
                val duration = phaseList[position].duration
                val duration_rest = phaseList[position].duration_rest
                val repetitions = phaseList[position].repetitions
                val timerId = phaseList[position].timerId
                val id = phaseList[position].id
                val removePhase = Phase(name, duration, duration_rest, repetitions, timerId, id)
                localPhaseList.remove(removePhase)
                displayPhasesInfo()
            }

            // Check correction of name field.
            override fun afterTextChangedName(position: Int, s: String) {
                val phaseList = adapter.currentList.toMutableList()
                val name = phaseList[position].name
                val duration = phaseList[position].duration
                val duration_rest = phaseList[position].duration_rest
                val repetitions = phaseList[position].repetitions
                val timer_id = timer?.id
                val id = phaseList[position].id
                if(s.length > 20)
                {
                    etPhaseName.setText(name)
                }
                else
                {
                    lifecycleScope.launch {
                        val updatePhase = Phase(name, duration, duration_rest, repetitions, timer_id, id)
                        phaseDatabase?.updatePhase(updatePhase)
                    }
//                    localPhaseList.find { it.id == id }?.name = s
                }
                displayPhasesInfo()
            }

            // Check correction of duration field.
            override fun afterTextChangedDuration(position: Int, s: String) {
                val phaseList = adapter.currentList.toMutableList()
                val duration = phaseList[position].duration
                val id = phaseList[position].id
                try {
                    if(s.length == 0)
                    {
                        etPhaseDuration.setText("0")
                        localPhaseList.find { it.id == id }?.duration = 0
                    }
                    // тут была проверка когда стоит 0, перезаписывать вводимым числом
//                    else if (localPhaseList.find { it.id == id }?.duration == 0)
//                    {
//                        val value = s.substring(1, s.length)
//                        Log.d("value", value)
//                        Log.d("s", s)
//                        etPhaseDuration.setText(value)
//                        localPhaseList.find { it.id == id }?.duration = value.toInt()
//                        return
//                    }
                    else if(s.toInt() > 999)
                    {
                        etPhaseDuration.setText(duration.toString())
                    }
                    else{
                        localPhaseList.find { it.id == id }?.duration = s.toInt()
                    }
                } catch (ex: NumberFormatException) {
                    etPhaseDuration.setText(duration.toString())
                    return
                }
                displayPhasesInfo()
            }

            // Check correction of duration rest field.
            override fun afterTextChangedDurationRest(position: Int, s: String) {
                val phaseList = adapter.currentList.toMutableList()
                val duration_rest = phaseList[position].duration_rest
                val id = phaseList[position].id
                try {
                    if(s.length == 0)
                    {
                        etPhaseDuration.setText("0")
                        localPhaseList.find { it.id == id }?.duration_rest = 0
                    }
                    else if(s.toInt() > 999)
                    {
                        etPhaseDurationRest.setText(duration_rest.toString())
                    }
                    else
                    {
                        localPhaseList.find { it.id == id }?.duration_rest = s.toInt()
                    }
                } catch (ex: NumberFormatException) {
                    etPhaseDurationRest.setText(duration_rest.toString())
                    return
                }
                displayPhasesInfo()
            }

            // Check correction of repetitions field.
            override fun afterTextChangedRepetitions(position: Int, s: String) {
                val phaseList = adapter.currentList.toMutableList()
                val repetitions = phaseList[position].repetitions
                val id = phaseList[position].id
                try {
                    if(s.length == 0)
                    {
                        etPhaseDuration.setText("0")
                        localPhaseList.find { it.id == id }?.repetitions = 0
                    }
                    else if(s.toInt() > 19)
                    {
                        etPhaseRepetitions.setText(repetitions.toString())
                    }
                    localPhaseList.find { it.id == id }?.repetitions = s.toInt()
                    displayPhasesInfo()
                } catch (ex: NumberFormatException) {
                    etPhaseRepetitions.setText(repetitions.toString())
                    return
                }
                displayPhasesInfo()
            }
        })

        etTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length > 16)
                {
                    etTitle.setText(localTimerTitle)
                    return
                }
                localTimerTitle = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        recyclerView.adapter = adapter
    }


    private fun displayTimerInfo()
    {
        tvTitle.text = timer?.title
        localTimerTitle = timer?.title.toString()
        etTitle.setText(timer?.title)
        val color = timer?.color
        if(color?.toInt() == R.color.title_green) color_green.isChecked = true
        else if (color?.toInt() == R.color.title_aqua) color_aqua.isChecked = true
        else if (color?.toInt() == R.color.title_yellow) color_yellow.isChecked = true
        else if (color?.toInt() == R.color.title_red) color_red.isChecked = true
        else if (color?.toInt() == R.color.title_pink) color_pink.isChecked = true
        else if (color?.toInt() == R.color.title_blue) color_blue.isChecked = true
    }


    private fun displayPhasesInfo()
    {
        Log.d("adapter.currentList", adapter.currentList.toMutableList().toString())
        lifecycleScope.launch {
            Log.d("localPhaseList", localPhaseList.toString())
            if (localPhaseList.isNotEmpty()) {
                adapter.submitList(localPhaseList)
            }
        }
    }


    private fun displayPhasesInfoWithScroll()
    {
        Log.d("adapter.currentList", adapter.currentList.toMutableList().toString())
        lifecycleScope.launch {
            Log.d("localPhaseList", localPhaseList.toString())
            if (localPhaseList.isNotEmpty()) {
                adapter.submitList(localPhaseList)
                phases_list.smoothScrollToPosition(localPhaseList.size-1)
            }
        }
    }


    private fun createUnsavedPhase()
    {
        val name = "phase name"
        val duration = 40
        val duration_rest = 10
        val repetitions = 0
        val timerId = timer?.id
        val phase = Phase(name, duration, duration_rest, repetitions, timerId)
        localPhaseList.add(phase)
        displayPhasesInfoWithScroll()
        Log.d("localPhaseList", "createUnsavedPhase --->" + localPhaseList.toString())
    }


    private fun saveTimerInfo()
    {
        val title = etTitle.text.toString()
        val color : String
        if (color_green.isChecked) color = R.color.title_green.toString()
        else if (color_yellow.isChecked) color = R.color.title_yellow.toString()
        else if (color_red.isChecked) color = R.color.title_red.toString()
        else if (color_pink.isChecked) color = R.color.title_pink.toString()
        else if (color_blue.isChecked) color = R.color.title_blue.toString()
        else if (color_aqua.isChecked) color = R.color.title_aqua.toString()
        else color = "000000"
        var duration = 0

        val updateTimer = Timer(title, color, duration, timer?.id)
        lifecycleScope.launch {
            phaseDatabase?.deletePhasesByTimerId(timer?.id)
            for (addPhase in localPhaseList)
            {
                phaseDatabase?.addPhase(addPhase)
                duration += (addPhase.duration + addPhase.duration_rest) * addPhase.repetitions
            }
            localPhaseList = mutableListOf()

            timerDatabase?.updateTimer(updateTimer)
        }
        this.timer = updateTimer
    }


    private fun initTimerInfo()
    {
        val intent = getIntent()
        val id = intent.getIntExtra("timer_id", -1)
        val title = intent.getStringExtra("timer_title").toString()
        val color = intent.getStringExtra("timer_color").toString()
        val duration = intent.getIntExtra("timer_duration", -1)
        this.timer = Timer(title, color, duration, id)
        tvTitle.text = timer?.title
        etTitle.setText(timer?.title)
        if(color.toInt() == R.color.title_green) color_green.isChecked = true
        else if (color.toInt() == R.color.title_aqua) color_aqua.isChecked = true
        else if (color.toInt() == R.color.title_yellow) color_yellow.isChecked = true
        else if (color.toInt() == R.color.title_red) color_red.isChecked = true
        else if (color.toInt() == R.color.title_pink) color_pink.isChecked = true
        else if (color.toInt() == R.color.title_blue) color_blue.isChecked = true
    }


    fun returnHome() {
        val home_intent = Intent(applicationContext, MainActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(home_intent)
    }


    private fun showToast(message:String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }

}
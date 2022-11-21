package com.example.lab2

import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.phase_adapter.*
import kotlinx.coroutines.launch


class EditActivity : BaseActivityTheme() {

    private val phaseDatabase by lazy { AppDatabase.getDatabase(this).phaseDao() }
    private val timerDatabase by lazy { AppDatabase.getDatabase(this).timerDao() }
    private lateinit var adapter: PhaseAdapter
    private var timer: Timer? = null
    private var localPhaseList: MutableList<Phase> = mutableListOf()
    private var localTimerTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setRecyclerView()
        initTimerInfo()
        displayPhasesInfo()

//        lifecycleScope.launch {
//            phaseDatabase?.getPhasesByTimerId(timer?.id)?.collect { phaseList ->
//                localPhaseList = phaseList.toMutableList()
//                if (localPhaseList.isNotEmpty()) {
//                    adapter.submitList(localPhaseList)
//                    phases_list.smoothScrollToPosition(localPhaseList.size-1)
//                }
//            }
//        }

        btnPhaseNew.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val name = getString(R.string.default_phase_name)
                val duration = 4
                val duration_rest = 10
                val repetitions = 1
                val timerId = timer?.id
                val phase = Phase(name, duration, duration_rest, repetitions, timerId)
                lifecycleScope.launch {
                    phaseDatabase?.addPhase(phase)
                    phaseDatabase?.getPhasesByTimerId(timer?.id)?.collect { phaseList ->
                        if (phaseList.isNotEmpty()) {
                            phases_list.smoothScrollToPosition(phaseList.size)
                        }
                    }
                    recalculateTimerDuration()
                }
            }
        })
        btnTimerSave.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                saveTimerInfo()
                displayTimerInfo()
            }
        })

    }

    override fun onResume() {
        super.onResume()
    }


    private fun setRecyclerView()
    {
        // устанавливаем для списка адаптер
        val recyclerView = findViewById<RecyclerView>(R.id.phases_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        adapter = PhaseAdapter(this)

        adapter.setItemListener(object : RecyclerClickListenerPhase {
            // Tap the btnDelete to delete the phase.
            override fun onItemRemoveClick(position: Int) {
                val phaseList = adapter.currentList.toMutableList()
                val name = phaseList[position].name
                val duration = phaseList[position].duration
                val duration_rest = phaseList[position].duration_rest
                val repetitions = phaseList[position].repetitions
                val timerId = phaseList[position].timerId
                val id = phaseList[position].id
                val removePhase = Phase(name, duration, duration_rest, repetitions, timerId, id)
                lifecycleScope.launch {
                    phaseDatabase?.deletePhase(removePhase)
                    recalculateTimerDuration()
                }
            }

            // Tap the btnEdit to edit the phase.
            override fun onItemEditClick(position: Int) {
                val intent = Intent(this@EditActivity, EditPhase::class.java)
                val phasesList = adapter.currentList.toMutableList()
                intent.putExtra("name", phasesList[position].name)
                intent.putExtra("duration", phasesList[position].duration)
                intent.putExtra("duration_rest", phasesList[position].duration_rest)
                intent.putExtra("repetitions", phasesList[position].repetitions)
                intent.putExtra("timer_id", phasesList[position].timerId)
                intent.putExtra("id", phasesList[position].id)
                startActivity(intent)
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
            phaseDatabase?.getPhasesByTimerId(timer?.id)?.collect { phaseList ->
                if (phaseList.isNotEmpty()) {
                    adapter.submitList(phaseList)
//                    phases_list.smoothScrollToPosition(localPhaseList.size-1)
                }
            }
        }
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

        // TODO: почемуто цвет не применяется

        val updateTimer = Timer(title, color, timer?.duration!!.toInt(), timer?.id)
        lifecycleScope.launch {
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

    private fun recalculateTimerDuration(){
        // TODO: сделать апдейт timer duration
        lifecycleScope.launch {
            var duration = timer?.duration!!.toInt()
            phaseDatabase?.getPhasesByTimerId(timer?.id)?.collect { phaseList ->
                if (phaseList.isNotEmpty()) {
                    for (phase in phaseList){
                        duration += (phase.duration + phase.duration_rest) * phase.repetitions
                    }
                }
            }
            timer?.duration = duration
            // TODO: почему то логи не выводятся
            Log.d("new duration -----", duration.toString())
            timerDatabase?.updateTimer(timer!!)
        }
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
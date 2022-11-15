package com.example.lab2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_edit_phase.*
import kotlinx.coroutines.launch

class EditPhase : BaseActivityTheme() {
    private val phaseDatabase by lazy { AppDatabase.getDatabase(this).phaseDao() }
    private val timerDatabase by lazy { AppDatabase.getDatabase(this).timerDao() }
    private var name: String? = null
    private var duration: Int? = null
    private var duration_rest: Int? = null
    private var repetitions: Int? = null
    private var timer_id: Int? = null
    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_phase)
        this.name = intent.getStringExtra("name")
        this.duration = intent.getIntExtra("duration", -1)
        this.duration_rest = intent.getIntExtra("duration_rest", -1)
        this.repetitions = intent.getIntExtra("repetitions", -1)
        this.timer_id = intent.getIntExtra("timer_id", -1)
        this.id = intent.getIntExtra("id", -1)

        etPhaseName.setText(name)
        etPhaseDuration.setText(duration.toString())
        etPhaseDurationRest.setText(duration_rest.toString())
        etPhaseRepetitions.setText(repetitions.toString())


        etPhaseName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val new_name = s.toString()
                if(new_name.length > 20)
                {
                    etPhaseName.setText(name)
                }
                else
                {
                    name = new_name
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        etPhaseDuration.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    val new_duration = s.toString().toInt()
                    if(new_duration > 3600)
                    {
                        etPhaseDuration.setText(duration.toString())
                        showToast("You cannot set duration above 1 hour")
                    }
                    else
                    {
                        duration = new_duration
                    }
                } catch (ex: NumberFormatException) {
                    return
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        etPhaseDurationRest.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                try {
                    val new_duration_rest = s.toString().toInt()
                    if(new_duration_rest > 3600)
                    {
                        etPhaseDurationRest.setText(duration_rest.toString())
                        showToast("You cannot set rest duration above 1 hour")
                    }
                    else
                    {
                        duration_rest = new_duration_rest
                    }
                } catch (ex: NumberFormatException) {
                    return
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        etPhaseRepetitions.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.d("afterTextChanged", "-----------")
                Log.d("etphase text", etPhaseRepetitions.text.toString())
                Log.d("text to change", s.toString())
                try {
                    val new_repetitions = s.toString().toInt()
                    if(new_repetitions > 50)
                    {
                        etPhaseRepetitions.setText(repetitions.toString())
                        showToast("You cannot set repetitions above 50")
                    }
                    else
                    {
                        repetitions = new_repetitions
                    }
                } catch (ex: NumberFormatException) {
                    repetitions = 0
                    return
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        btnPhaseSave.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                lifecycleScope.launch {
                    var isValid = true
                    try {
                        etPhaseDuration.text.toString().toInt()
                    } catch (ex: NumberFormatException) {
                        isValid = false
                        showToast("Phase duration must be a valid integer")
                    }
                    try {
                        etPhaseDurationRest.text.toString().toInt()
                    } catch (ex: NumberFormatException) {
                        isValid = false
                        showToast("Phase rest duration must be a valid integer")
                    }
                    try {
                        etPhaseRepetitions.text.toString().toInt()
                    } catch (ex: NumberFormatException) {
                        isValid = false
                        showToast("Repetitions must be a valid integer")
                    }
                    if (isValid) {
                        val updatePhase = Phase(
                            name!!,
                            duration!!,
                            duration_rest!!,
                            repetitions!!,
                            timer_id!!,
                            id!!
                        )
                        phaseDatabase?.updatePhase(updatePhase)
                        Log.d("after update phase", "----------- exec ------------")
                        recalculateTimerDuration()
//                        finish()
                    }
                }
            }
        })
    }

    private fun recalculateTimerDuration(){
        // TODO: сделать апдейт timer duration
        lifecycleScope.launch {
            var timer = timerDatabase?.getTimerById(timer_id!!)
            var duration = 0
            Log.d("before phases list", "------------")
            phaseDatabase?.getPhasesByTimerId(timer_id)?.collect { phaseList ->
                // TODO: вылетает обращение к бд
                Log.d("phaseList --------->", phaseList.toString())
                if (phaseList.isNotEmpty()) {
                    for (phase in phaseList){
                        duration += (phase.duration + phase.duration_rest) * phase.repetitions
                    }
                }
            }
            Log.d("new duration", "------------- " + duration + " -------------")
            timer?.duration = duration
            timerDatabase?.updateTimer(timer!!)
        }
    }

    private fun showToast(message:String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
}
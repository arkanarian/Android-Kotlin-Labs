package com.example.lab2


import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.phase_adapter.*

class PhaseAdapter internal constructor(context: Context?, ids: MutableList<Int>, names: MutableList<String>, phase_duration: MutableList<Int>, rest_duration: MutableList<Int>, repetitions: MutableList<Int>) :
    RecyclerView.Adapter<PhaseAdapter.MyViewHolder>() {
    private val inflater: LayoutInflater
    private val ids: MutableList<Int>
    private val names: MutableList<String>
    private val phase_duration: MutableList<Int>
    private val rest_duration: MutableList<Int>
    private val repetitions: MutableList<Int>

    init {
        this.ids = ids
        this.names = names
        this.phase_duration = phase_duration
        this.rest_duration = rest_duration
        this.repetitions = repetitions
        inflater = LayoutInflater.from(context)
    }

    // onCreateViewHolder: возвращает объект ViewHolder, который будет хранить данные по одному объекту State
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = inflater.inflate(R.layout.timer_home, parent, false)
        return MyViewHolder(view)
    }

    // onBindViewHolder: выполняет привязку объекта ViewHolder к объекту State по определенной позиции.
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.idView.setText(this.ids.get(position).toString())
        holder.nameView.setText(this.names.get(position))
        holder.phaseDurationView.setText(this.phase_duration.get(position).toString())
        holder.restDurationView.setText(this.rest_duration.get(position).toString())
        holder.repetitionsView.setText(this.repetitions.get(position).toString())
    }

    // getItemCount: возвращает количество объектов в списке
    override fun getItemCount(): Int {
        return ids.size
    }

    class MyViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView
        val nameView: EditText
        val phaseDurationView: EditText
        val restDurationView: EditText
        val repetitionsView: EditText

        init {
            idView = view.findViewById(R.id.tvPhaseId)
            nameView = view.findViewById(R.id.etPhaseName)
            phaseDurationView = view.findViewById(R.id.etPhaseDuration)
            restDurationView = view.findViewById(R.id.etRestDuration)
            repetitionsView = view.findViewById(R.id.etRepetitions)
        }
    }
}
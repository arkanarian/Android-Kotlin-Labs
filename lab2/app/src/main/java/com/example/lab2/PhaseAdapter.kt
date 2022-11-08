package com.example.lab2


import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.phase_adapter.*

class PhaseAdapter : ListAdapter<Phase, PhaseAdapter.MyViewHolder>(DiffCallback()) {
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var listener: RecyclerClickListenerPhase
    fun setItemListener(listener: RecyclerClickListenerPhase) {
        this.listener = listener
    }

    // onCreateViewHolder: возвращает объект ViewHolder, который будет хранить данные по одному объекту State
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.phase_adapter, parent, false)
        val phaseHolder = MyViewHolder(v)

        val phaseDelete = phaseHolder.itemView.findViewById<Button>(R.id.btnPhaseDelete)
        phaseDelete.setOnClickListener {
            listener.onItemRemoveClick(phaseHolder.adapterPosition)
        }

        val etName = phaseHolder.itemView.findViewById<EditText>(R.id.etPhaseName)
        etName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                listener.afterTextChangedName(phaseHolder.adapterPosition, s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        val etDuration = phaseHolder.itemView.findViewById<EditText>(R.id.etPhaseDuration)
        etDuration.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                listener.afterTextChangedDuration(phaseHolder.adapterPosition, s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        val etDurationRest = phaseHolder.itemView.findViewById<EditText>(R.id.etPhaseDurationRest)
        etDurationRest.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                listener.afterTextChangedDurationRest(phaseHolder.adapterPosition, s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        val etRepetitions = phaseHolder.itemView.findViewById<EditText>(R.id.etPhaseRepetitions)
        etRepetitions.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                listener.afterTextChangedRepetitions(phaseHolder.adapterPosition, s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        return phaseHolder
    }

    // onBindViewHolder: выполняет привязку объекта ViewHolder к объекту State по определенной позиции.
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = getItem(position)
        val phaseName = holder.itemView.findViewById<EditText>(R.id.etPhaseName)
        val duration = holder.itemView.findViewById<EditText>(R.id.etPhaseDuration)
        val duration_rest = holder.itemView.findViewById<EditText>(R.id.etPhaseDurationRest)
        val repetitions = holder.itemView.findViewById<EditText>(R.id.etPhaseRepetitions)
        phaseName.setText(currentItem.name)
        duration.setText(currentItem.duration.toString())
        duration_rest.setText(currentItem.duration_rest.toString())
        repetitions.setText(currentItem.repetitions.toString())
    }


    class DiffCallback : DiffUtil.ItemCallback<Phase>() {
        override fun areContentsTheSame(oldItem: Phase, newItem: Phase) =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: Phase, newItem: Phase) =
            oldItem.id == newItem.id
    }
}
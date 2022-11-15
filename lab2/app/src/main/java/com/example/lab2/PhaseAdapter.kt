package com.example.lab2


import android.content.Context
import android.graphics.Color
import android.provider.Settings.Global.getString
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

class PhaseAdapter(context: Context) : ListAdapter<Phase, PhaseAdapter.MyViewHolder>(DiffCallback()) {
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    var context: Context? = null
    init {
        this.context = context
    }
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

        val phaseEdit = phaseHolder.itemView.findViewById<Button>(R.id.btnPhaseEdit)
        phaseEdit.setOnClickListener {
            listener.onItemEditClick(phaseHolder.adapterPosition)
        }

        return phaseHolder
    }

    // onBindViewHolder: выполняет привязку объекта ViewHolder к объекту State по определенной позиции.
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = getItem(position)
        val phaseName = holder.itemView.findViewById<TextView>(R.id.tvPhaseName)
        val duration = holder.itemView.findViewById<TextView>(R.id.tvDuration)
        val duration_rest = holder.itemView.findViewById<TextView>(R.id.tvDurationRest)
        val repetitions = holder.itemView.findViewById<TextView>(R.id.tvRepetitions)
        phaseName.setText(currentItem.name)
        duration.setText(currentItem.duration.toString() + " " + context?.getString(R.string.ending_duration)!!)
        duration_rest.setText(currentItem.duration_rest.toString() + " " + context?.getString(R.string.ending_duration_rest)!!)
        repetitions.setText(currentItem.repetitions.toString() + context?.getString(R.string.ending_repetitions)!!)
    }


    class DiffCallback : DiffUtil.ItemCallback<Phase>() {
        override fun areContentsTheSame(oldItem: Phase, newItem: Phase) =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: Phase, newItem: Phase) =
            oldItem.id == newItem.id
    }
}
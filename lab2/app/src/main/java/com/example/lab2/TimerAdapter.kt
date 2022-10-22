package com.example.lab2

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TimerAdapter internal constructor(context: Context?, states: List<State>) :
    RecyclerView.Adapter<TimerAdapter.ViewHolder>() {
    private val inflater: LayoutInflater
    private val states: List<State>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val state: State = states[position]
        holder.flagView.setImageResource(state.getFlagResource())
        holder.nameView.setText(state.getName())
        holder.capitalView.setText(state.getCapital())
    }

    override fun getItemCount(): Int {
        return states.size
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val flagView: ImageView
        val nameView: TextView
        val capitalView: TextView

        init {
            flagView = view.findViewById(R.id.flag)
            nameView = view.findViewById(R.id.name)
            capitalView = view.findViewById(R.id.capital)
        }
    }

    init {
        this.states = states
        inflater = LayoutInflater.from(context)
    }
}
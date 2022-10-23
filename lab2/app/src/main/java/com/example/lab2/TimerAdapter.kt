package com.example.lab2

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.timer_home.*

class TimerAdapter internal constructor(context: Context?, ids: MutableList<Int>, titles: MutableList<String>, colors: MutableList<String>) :
    RecyclerView.Adapter<TimerAdapter.MyViewHolder>() {
    private val inflater: LayoutInflater
    private val ids: MutableList<Int>
    private val titles: MutableList<String>
    private val colors: MutableList<String>

    init {
        this.ids = ids
        this.titles = titles
        this.colors = colors
        inflater = LayoutInflater.from(context)
    }

    // onCreateViewHolder: возвращает объект ViewHolder, который будет хранить данные по одному объекту State
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = inflater.inflate(R.layout.timer_home, parent, false)
        return MyViewHolder(view)
    }

    // onBindViewHolder: выполняет привязку объекта ViewHolder к объекту State по определенной позиции.
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.titleView.setText(this.titles.get(position))
        holder.titleView.setBackgroundColor(Color.parseColor("#" + this.colors.get(position)))
        holder.idView.setText(this.ids.get(position).toString())
    }

    // getItemCount: возвращает количество объектов в списке
    override fun getItemCount(): Int {
        return ids.size
    }

    class MyViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView
        val titleView: TextView
        val durationView: TextView
        val editView: Button
        val deleteView: Button

        init {
            idView = view.findViewById(R.id.tvTimerId)
            titleView = view.findViewById(R.id.tvTimerTitle)
            durationView = view.findViewById(R.id.tvTimerDuration)
            editView = view.findViewById(R.id.btnEdit)
            deleteView = view.findViewById(R.id.btnDelete)
        }
    }
}
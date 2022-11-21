package com.example.lab2

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.timer_home.*
import java.security.AccessController.getContext

class TimerAdapter(context: Context) : ListAdapter<Timer, TimerAdapter.MyViewHolder>(DiffCallback()) {
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    var context: Context? = null
    init {
        this.context = context
    }
    private lateinit var listener: RecyclerClickListener
    fun setItemListener(listener: RecyclerClickListener) {
        this.listener = listener
    }

    // onCreateViewHolder: возвращает объект ViewHolder, который будет хранить данные по одному объекту State
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.timer_home, parent, false)
        val timerHolder = MyViewHolder(v)

        val timerDelete = timerHolder.itemView.findViewById<Button>(R.id.btnTimerDelete)
        timerDelete.setOnClickListener {
            listener.onItemRemoveClick(timerHolder.adapterPosition)
        }

        // было CardView
        val timerEdit = timerHolder.itemView.findViewById<Button>(R.id.btnTimerEdit)
        timerEdit.setOnClickListener {
            listener.onItemEditClick(timerHolder.adapterPosition)
        }

        val timer = timerHolder.itemView.findViewById<LinearLayout>(R.id.linear_timer)
        timer.setOnClickListener {
            listener.onItemClick(timerHolder.adapterPosition)
        }



        return timerHolder
    }

    // onBindViewHolder: выполняет привязку объекта ViewHolder к объекту Timer по определенной позиции.
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = getItem(position)
        val timerTitle = holder.itemView.findViewById<TextView>(R.id.tvTimerTitle)
        timerTitle.text = currentItem.title
        timerTitle.setBackgroundResource(currentItem.color.toInt())
//        timerTitle.setBackgroundColor(Color.parseColor("#" + currentItem.color))
        val timerDuration = holder.itemView.findViewById<TextView>(R.id.tvTimerDuration)
        timerDuration.text = currentItem.duration.toString() + " " + context?.getString(R.string.sec)!!
    }

    class DiffCallback : DiffUtil.ItemCallback<Timer>() {
        override fun areContentsTheSame(oldItem: Timer, newItem: Timer) =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: Timer, newItem: Timer) =
            oldItem.id == newItem.id
    }
}
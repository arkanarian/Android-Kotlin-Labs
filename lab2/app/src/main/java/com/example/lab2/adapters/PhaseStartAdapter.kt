package com.example.lab2.adapters



import android.content.Context
import android.content.res.ColorStateList
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
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lab2.Phase
import com.example.lab2.R
import com.example.lab2.RecyclerClickListenerPhase
import com.example.lab2.RecyclerClickListenerPhaseStart
import kotlinx.android.synthetic.main.phase_adapter.*

class PhaseStartAdapter(context: Context) : ListAdapter<Phase, PhaseStartAdapter.MyViewHolder>(DiffCallback()) {
    var selectedIndex: Int = 0
    var reps_done: Int = 0
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    var context: Context? = null
    init {
        this.context = context
    }
    private lateinit var listener: RecyclerClickListenerPhaseStart
    fun setItemListener(listener: RecyclerClickListenerPhaseStart) {
        this.listener = listener
    }

    // onCreateViewHolder: возвращает объект ViewHolder, который будет хранить данные по одному объекту State
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.phase_start_adapter, parent, false)
        val phaseHolder = MyViewHolder(v)
        return phaseHolder
    }

    // onBindViewHolder: выполняет привязку объекта ViewHolder к объекту State по определенной позиции.
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = getItem(position)
        val phaseName = holder.itemView.findViewById<TextView>(R.id.tvPhaseName)
        val duration = holder.itemView.findViewById<TextView>(R.id.tvDuration)
        val duration_rest = holder.itemView.findViewById<TextView>(R.id.tvDurationRest)
        val repetitions = holder.itemView.findViewById<TextView>(R.id.tvRepetitions)
        val back = holder.itemView.findViewById<LinearLayout>(R.id.back)
        val reps_count = holder.itemView.findViewById<TextView>(R.id.tvRepetitionsCount)
        val title_reps_count = holder.itemView.findViewById<TextView>(R.id.tvTitleRepsCount)
        if (selectedIndex == position) {
//            back.setBackgroundColor(Color.parseColor("#E49B83"))
//            back.back(Color.parseColor("#E49B83"))
//            back.getBackground().setTint(Color.RED)

            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val isDarkTheme = sharedPreferences.getBoolean("app_theme", true)
            if (isDarkTheme)
            {
                back.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E49B83"))
                back.setBackgroundColor(Color.parseColor("#E49B83"))
            }
            else
            {
                back.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E49B83"))
                back.setBackgroundColor(Color.parseColor("#E49B83"))
            }
        }
        else {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val isDarkTheme = sharedPreferences.getBoolean("app_theme", true)
            if (isDarkTheme)
            {
                back.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                back.setBackgroundColor(Color.parseColor("#000000"))
            }
            else
            {
                back.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                back.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
        }
        if (selectedIndex == position) {
            reps_count.visibility = View.VISIBLE
            title_reps_count.visibility = View.VISIBLE
            reps_count.setText(reps_done.toString() + "/" + currentItem.repetitions.toString())
        }
        else {
            title_reps_count.visibility = View.INVISIBLE
            reps_count.visibility = View.INVISIBLE
        }
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
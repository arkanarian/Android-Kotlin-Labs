package com.example.lab2

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.phase_adapter.*
import kotlinx.android.synthetic.main.phase_adapter.view.*

class EditActivity : AppCompatActivity() {

//    private var titleText: EditText? = null
//    private var updateBtn: Button? = null, private  var deleteBtn:Button? = null
//    private var descText: EditText? = null

    private var id: Int = -1
    private var title: String = ""
    private var color: String = ""
    private var ids : MutableList<Int> = ArrayList()
    private var names : MutableList<String> = ArrayList()
    private var phase_duration : MutableList<Int> = ArrayList()
    private var rest_duration : MutableList<Int> = ArrayList()
    private var repetitions : MutableList<Int> = ArrayList()

    private var dbManagerTimer: DBManagerTimer? = null
    private var dbManagerPhase: DBManagerPhase? = null

    private var adapter: PhaseAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        dbManagerTimer = DBManagerTimer(this)
        dbManagerPhase = DBManagerPhase(this)
        dbManagerTimer?.open()
        dbManagerPhase?.open()

        // display title and color of timer
        val intent = getIntent()
        id = intent.getIntExtra("id", -1)
        Log.d("id", id.toString())
        val cursor = dbManagerTimer!!.fetch(id)
        if(cursor != null){
            title = cursor.getString(1)
            color = cursor.getString(2)
        }
        tvTitle.setText(title)
        etTitle.setText(title)

        // устанавливаем для списка адаптер
        val recyclerView = findViewById<RecyclerView>(R.id.recPhases)
        recyclerView.adapter = adapter
        ids = arrayListOf()
        names = arrayListOf()
        phase_duration = arrayListOf()
        rest_duration = arrayListOf()
        repetitions = arrayListOf()
        adapter = PhaseAdapter(this, ids, names, phase_duration, rest_duration, repetitions)
        recyclerView.setAdapter(adapter)
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        // display data of phases
        displayPhasesData()

    }


    fun OnClickDeletePhase(view: View)
    {
        // remove phase tvPhaseId.text.toString().toInt()
    }

    private fun displayPhasesData()
    {
        val cursor: Cursor? = dbManagerPhase!!.fetch()
        if(cursor?.count == 0){
            return
        }
        else
        {
            while (cursor?.moveToNext() == true) {
                ids.add(cursor.getInt(0))
                names.add(cursor.getString(1))
                phase_duration.add(cursor.getInt(2))
                rest_duration.add(cursor.getInt(3))
                repetitions.add(cursor.getInt(4))
            }
        }
    }


//    fun onClick(v: View) {
//        when (v.getId()) {
//            android.R.id.btn_update -> {
//                val title = titleText!!.text.toString()
//                val desc = descText!!.text.toString()
//                dbManager.update(_id, title, desc)
//                returnHome()
//            }
//            android.R.id.btn_delete -> {
//                dbManager.delete(_id)
//                returnHome()
//            }
//        }
//    }
//
    fun returnHome() {
        val home_intent = Intent(applicationContext, MainActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(home_intent)
    }

    private fun showToast(message:String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
}
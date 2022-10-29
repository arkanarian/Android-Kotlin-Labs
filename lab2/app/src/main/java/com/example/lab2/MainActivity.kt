package com.example.lab2

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.timer_home.*
import kotlinx.android.synthetic.main.timer_home.view.*


class MainActivity : AppCompatActivity() {

    private var dbManager: DBManagerTimer? = null
    var db = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "timerapp-database"
    ).build()
    private var adapter: TimerAdapter? = null
    private var ids : MutableList<Int> = ArrayList()
    private var titles : MutableList<String> = ArrayList()
    private var colors : MutableList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val timerDao: TimerDao? = (applicationContext as MyDatabaseApplication).getAppDatabase()
            ?.timerDao()


        dbManager = DBManagerTimer(this)
        dbManager!!.open()

        // устанавливаем для списка адаптер
        val recyclerView = findViewById<RecyclerView>(R.id.timer_list)
        recyclerView.adapter = adapter
        ids = arrayListOf()
        titles = arrayListOf()
        colors = arrayListOf()
        adapter = TimerAdapter(this, ids, titles, colors)
        recyclerView.setAdapter(adapter)
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        displayData()
        Log.d("Titles", titles.joinToString(separator = ", "))
        Log.d("Colors", colors.joinToString(separator = ", "))

        // OnCLickListiner For List Items
//        recyclerView.onItemClick(OnItemClickListener { parent, view, position, viewId ->
//            val idTextView = view.findViewById(R.id.id) as TextView
//            val titleTextView = view.findViewById(R.id.title) as TextView
//            val descTextView = view.findViewById(R.id.desc) as TextView
//            val id = idTextView.text.toString()
//            val title = titleTextView.text.toString()
//            val desc = descTextView.text.toString()
//            val modify_intent = Intent(applicationContext, ModifyCountryActivity::class.java)
//            modify_intent.putExtra("title", title)
//            modify_intent.putExtra("desc", desc)
//            modify_intent.putExtra("id", id)
//            startActivity(modify_intent)
//        })
        recyclerView.onItemClick { recyclerView, position, v ->
//            var colorCode : Int
//            Log.d("hello", "hiiii")
//            if (tvTimerTitle.getBackground() is ColorDrawable) {
//                val cd = tvTimerTitle.getBackground() as ColorDrawable
//                colorCode = cd.color
//                Log.d("colorCode", colorCode.toString())
//            }
            val modify_intent = Intent(this, EditActivity::class.java)
            modify_intent.putExtra("id", v.tvTimerId.text.toString().toInt())
            startActivity(modify_intent)
        }
    }

    fun OnClickEdit(view: View)
    {
        val modify_intent = Intent(this, EditActivity::class.java)
        modify_intent.putExtra("id", tvTimerId.text.toString().toInt())
        startActivity(modify_intent)
    }

    private fun displayData()
    {
        val cursor: Cursor? = dbManager!!.fetch()
        if(cursor?.count == 0){
            showToast("No Timers")
            return
        }
        else
        {
            while (cursor?.moveToNext() == true) {
                ids.add(cursor.getInt(0))
                titles.add(cursor.getString(1))
                colors.add(cursor.getString(2))
            }
        }
    }

//    Android SQLite database tutorial
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    Android SQLite database tutorial
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id: Int = item.getItemId()
//        if (id == R.id.add_record) {
//            val add_mem = Intent(this, AddCountryActivity::class.java)
//            startActivity(add_mem)
//        }
//        return super.onOptionsItemSelected(item)
//    }


    private fun showToast(message:String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
}
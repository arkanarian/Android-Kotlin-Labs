package com.example.lab2

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    private var dbManager: DBManagerTimer? = null

    private var listView: ListView? = null

    private var adapter: SimpleCursorAdapter? = null

    val from = arrayOf(DatabaseHelper._ID, DatabaseHelper.SUBJECT, DatabaseHelper.DESC)

    val to = intArrayOf(R.id.id, R.id.title, R.id.desc)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_emp_list)
        dbManager = DBManager(this)
        dbManager.open()
        val cursor: Cursor = dbManager.fetch()
        listView = findViewById<View>(R.id.list_view) as ListView
        listView.setEmptyView(findViewById(R.id.empty))
        adapter = SimpleCursorAdapter(this, R.layout.activity_view_record, cursor, from, to, 0)
        adapter.notifyDataSetChanged()
        listView.setAdapter(adapter)

        // OnCLickListiner For List Items
        listView.setOnItemClickListener(OnItemClickListener { parent, view, position, viewId ->
            val idTextView = view.findViewById(R.id.id) as TextView
            val titleTextView = view.findViewById(R.id.title) as TextView
            val descTextView = view.findViewById(R.id.desc) as TextView
            val id = idTextView.text.toString()
            val title = titleTextView.text.toString()
            val desc = descTextView.text.toString()
            val modify_intent = Intent(applicationContext, ModifyCountryActivity::class.java)
            modify_intent.putExtra("title", title)
            modify_intent.putExtra("desc", desc)
            modify_intent.putExtra("id", id)
            startActivity(modify_intent)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        if (id == R.id.add_record) {
            val add_mem = Intent(this, AddCountryActivity::class.java)
            startActivity(add_mem)
        }
        return super.onOptionsItemSelected(item)
    }
}
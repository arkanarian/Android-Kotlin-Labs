package com.example.lab2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase

class DBManagerPhase(private val context: Context) {
    private var dbHelper: DatabaseHelper? = null
    private var database: SQLiteDatabase? = null
    @Throws(SQLException::class)
    fun open(): DBManagerPhase {
        dbHelper = DatabaseHelper(context)
        database = dbHelper!!.writableDatabase
        return this
    }

    fun close() {
        dbHelper!!.close()
    }

    fun insert(name: String?, duration: Int?, timer_id: Int?) {
        val contentValue = ContentValues()
        contentValue.put(DatabaseHelper.NAME_PH, name)
        contentValue.put(DatabaseHelper.DURATION_PH, duration)
        contentValue.put(DatabaseHelper.TIMER_ID_PH, timer_id)
        database!!.insert(DatabaseHelper.TABLE_NAME_PH, null, contentValue)
    }

    fun fetch(): Cursor? {
        val columns = arrayOf(DatabaseHelper._ID, DatabaseHelper.NAME_PH, DatabaseHelper.DURATION_PH, DatabaseHelper.TIMER_ID_PH)
        val cursor =
            database!!.query(DatabaseHelper.TABLE_NAME_PH, columns, null, null, null, null, null)
        cursor?.moveToFirst()
        return cursor
    }

    fun update(_id: Int, name: String?, duration: Int?, timer_id: Int?): Int {
        val contentValues = ContentValues()
        contentValues.put(DatabaseHelper.NAME_PH, name)
        contentValues.put(DatabaseHelper.DURATION_PH, duration)
        contentValues.put(DatabaseHelper.TIMER_ID_PH, timer_id)
        return database!!.update(
            DatabaseHelper.TABLE_NAME_PH,
            contentValues,
            DatabaseHelper._ID + " = " + _id,
            null
        )
    }

    fun delete(_id: Int) {
        database!!.delete(DatabaseHelper.TABLE_NAME_PH, DatabaseHelper._ID + "=" + _id, null)
    }
}
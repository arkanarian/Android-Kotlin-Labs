package com.journaldev.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.example.lab2.DatabaseHelper

class DBManagerTimer(private val context: Context) {
    private var dbHelper: DatabaseHelper? = null
    private var database: SQLiteDatabase? = null
    @Throws(SQLException::class)
    fun open(): DBManagerTimer {
        dbHelper = DatabaseHelper(context)
        database = dbHelper!!.writableDatabase
        return this
    }

    fun close() {
        dbHelper!!.close()
    }

    fun insert(title: String?, color: String?) {
        val contentValue = ContentValues()
        contentValue.put(DatabaseHelper.TITLE_T, title)
        contentValue.put(DatabaseHelper.COLOR_T, color)
        database!!.insert(DatabaseHelper.TABLE_NAME_T, null, contentValue)
    }

    fun fetch(): Cursor? {
        val columns = arrayOf(DatabaseHelper._ID, DatabaseHelper.TITLE_T, DatabaseHelper.COLOR_T)
        val cursor =
            database!!.query(DatabaseHelper.TABLE_NAME_T, columns, null, null, null, null, null)
        cursor?.moveToFirst()
        return cursor
    }

    fun update(_id: Long, title: String?, color: String?): Int {
        val contentValues = ContentValues()
        contentValues.put(DatabaseHelper.TITLE_T, title)
        contentValues.put(DatabaseHelper.COLOR_T, color)
        return database!!.update(
            DatabaseHelper.TABLE_NAME_T,
            contentValues,
            DatabaseHelper._ID + " = " + _id,
            null
        )
    }

    fun delete(_id: Long) {
        database!!.delete(DatabaseHelper.TABLE_NAME_T, DatabaseHelper._ID + "=" + _id, null)
    }
}
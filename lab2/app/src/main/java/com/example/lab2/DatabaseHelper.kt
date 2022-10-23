package com.example.lab2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_PH)
        db.execSQL(CREATE_TABLE_T)
        db.execSQL(FILL_TABLE_T)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_T)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PH)
        onCreate(db)
    }

    companion object {
        // Database Information
        const val DB_NAME = "TIMERS.DB"

        // database version
        const val DB_VERSION = 1

        // Table Name
        const val TABLE_NAME_T = "TIMERS"

        // Table columns
        const val _ID = "_id"
        const val TITLE_T = "title"
        const val COLOR_T = "color"

        // Table Phase Name
        const val TABLE_NAME_PH = "PHASES"

        // Table Phase columns
        const val TITLE_PH = "title"
        const val TYPE_PH = "type"
        const val TIMER_ID_PH = "timer_id"
        const val DURATION_PH = "duration"


        // Creating table timers query
        private const val CREATE_TABLE_T = ("create table " + TABLE_NAME_T + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE_T + " TEXT NOT NULL, " + COLOR_T + " CHAR(6) NOT NULL);")

        // Fill table timers
        private const val FILL_TABLE_T = ("INSERT INTO " + TABLE_NAME_T + "(" + TITLE_T + ", " + COLOR_T + ")"
                + "VALUES " + "('running', '123567');")

        // Creating table phases query
        private const val CREATE_TABLE_PH = ("create table " + TABLE_NAME_PH + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE_PH + " TEXT NOT NULL, "
                + DURATION_PH + " INTEGER NOT NULL,"
                + TYPE_PH + " TEXT NOT NULL,"
                + TIMER_ID_PH + " REFERENCES" + TABLE_NAME_T + ");")
    }
}
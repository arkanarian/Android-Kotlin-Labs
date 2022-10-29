package com.example.lab2

import android.app.Application
import androidx.room.Room


class MyDatabaseApplication : Application() {
    var myDatabase: AppDatabase? = null
    override fun onCreate() {
        super.onCreate()

        // when upgrading versions, kill the original tables by using fallbackToDestructiveMigration()
        myDatabase = Room.databaseBuilder(this, AppDatabase::class.java, AppDatabase.NAME)
            .fallbackToDestructiveMigration().build()
    }

    fun getAppDatabase(): AppDatabase? {
        return myDatabase
    }
}
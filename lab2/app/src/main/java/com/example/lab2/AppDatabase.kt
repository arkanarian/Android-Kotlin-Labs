package com.example.lab2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase



@Database(
    entities = [Timer::class /*, AnotherEntityType.class, AThirdEntityType.class */],
    version = 1,  // bump version number if your schema changes
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
//    companion object {
//
//
//
//        // Database name to be used
//        val NAME = "timerdb"
////        lateinit var instance: AppDatabase
////        syncronized fun getInstance(context: Context){
////            if(instance == null){
////                instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class.NAME)
////            .fallbackToDestructiveMigration()
////            .build()
////            }
////        }
//    }

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            if (INSTANCE == null) {
                synchronized(this) {
                    // Pass the database to the INSTANCE
                    INSTANCE = buildDatabase(context)
                }
            }
            // Return database.
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "timer_database"
            )
                .build()
        }
    }


    abstract fun timerDao(): TimerDao?
}
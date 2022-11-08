package com.example.lab2

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Timer::class, Phase::class /*, AnotherEntityType.class, AThirdEntityType.class */],
    version = 3,  // bump version number if your schema changes
    autoMigrations = [
        AutoMigration (from = 2, to = 3)
    ],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timerDao(): TimerDao?
    abstract fun phaseDao(): PhaseDao?
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
}
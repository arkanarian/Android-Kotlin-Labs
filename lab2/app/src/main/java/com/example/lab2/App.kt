package com.example.lab2

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.preference.PreferenceManager
import java.util.*


class App : Application() {
    companion object {
        val CHANNEL_ID = "lab2ServiceChannel"
        val CHANNEL_NAME = "Lab2 Service Channel"
    }


    override fun onCreate() {
        super.onCreate()

        var change = ""
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val language = sharedPreferences.getString("language", "bak")
        Log.d("Language --->", language!!)
        if (language == "English") {
            change="en"
        } else if (language=="Русский" ) {
            change = "ru"
        }else {
            change =""
        }
        Log.d("change --->", change!!)

        BaseActivity.dLocale = Locale(change) //set any locale you want here

        // notification channel

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Lab2 Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}

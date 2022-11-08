package com.example.lab2

import android.app.Application
import android.util.Log
import androidx.preference.PreferenceManager
import java.util.*

class App : Application() {

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

        BaseActivity.dLocale = Locale(change) //set any locale you want here
    }
}

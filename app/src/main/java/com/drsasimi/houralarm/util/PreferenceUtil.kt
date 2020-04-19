package com.drsasimi.houralarm.util

import android.content.Context

class PreferenceUtil(context: Context) {
    private val sharedPref = context.getSharedPreferences("com.drsasimi.houralarm.ALARM_PRESET", Context.MODE_PRIVATE)

    fun setUseAlarm(use: Boolean) {
        with(sharedPref.edit()) {
            putBoolean("use_alarm", use)
            commit()
        }
    }

    fun isUseAlarm(): Boolean {
        return sharedPref.getBoolean("use_alarm", false)
    }

    fun setEnableHour(hour: Int, use: Boolean) {
        with(sharedPref.edit()) {
            putBoolean("hour_$hour", use)
            commit()
        }
    }

    fun isEnableHour(hour: Int): Boolean {
        return sharedPref.getBoolean("hour_$hour", false)
    }
}
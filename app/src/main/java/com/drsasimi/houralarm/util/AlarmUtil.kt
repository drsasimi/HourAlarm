package com.drsasimi.houralarm.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.drsasimi.houralarm.receiver.AlarmReceiver
import java.util.*

class AlarmUtil {
    companion object {

        private fun pendingIntent(context: Context): PendingIntent {
            return Intent(context.applicationContext, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(context.applicationContext, 0, intent, 0)
            }
        }

        private fun getCurrentCalendar(): Calendar {
            return Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
            }
        }

        fun getCurrentHour(): Int {
            return getCurrentCalendar().get(Calendar.HOUR_OF_DAY)
        }

        fun nextMinute(minute: Int): Calendar {
            return Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                add(Calendar.MINUTE, minute)
            }
        }

        fun nextHour(hour: Int): Calendar {
            return Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                add(Calendar.HOUR, hour)
            }
        }

        fun destroy(context: Context) {
            val alarmIntent = pendingIntent(context)
            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmMgr.cancel(alarmIntent)
        }

        fun execute(context: Context, calendar: Calendar) {
            val alarmIntent = pendingIntent(context)
            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            alarmMgr.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )
        }
    }
}
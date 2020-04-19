package com.drsasimi.houralarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.RingtoneManager
import android.net.Uri
import com.drsasimi.houralarm.util.AlarmUtil
import com.drsasimi.houralarm.util.PreferenceUtil

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val prefUtil = PreferenceUtil(context)
        val currentHour = AlarmUtil.getCurrentHour()

        if (prefUtil.isEnableHour(currentHour)) {
//        val ringtoneUrl = RingtoneManager.getActualDefaultRingtoneUri(context.applicationContext, RingtoneManager.TYPE_NOTIFICATION)
            val ringtoneId = context.resources.getIdentifier("rices", "raw", "com.drsasimi.houralarm")
            val ringtoneUrl = Uri.parse("android.resource://com.drsasimi.houralarm/raw/${ringtoneId}")
            val ringtone = RingtoneManager.getRingtone(context.applicationContext, ringtoneUrl)
            ringtone.streamType = AudioManager.STREAM_ALARM
            ringtone.play()
        }

        AlarmUtil.execute(context, AlarmUtil.nextHour(1))
    }
}
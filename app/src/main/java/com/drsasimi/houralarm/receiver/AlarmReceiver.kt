package com.drsasimi.houralarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.RingtoneManager
import android.net.Uri
import com.drsasimi.houralarm.util.AlarmUtil
import com.drsasimi.houralarm.util.PreferenceUtil
import com.drsasimi.houralarm.util.RingtoneUtil

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val prefUtil = PreferenceUtil(context)
        val currentHour = AlarmUtil.getCurrentHour()

        if (prefUtil.isEnableHour(currentHour)) {
//        val ringtoneUrl = RingtoneManager.getActualDefaultRingtoneUri(context.applicationContext, RingtoneManager.TYPE_NOTIFICATION)
//            val ringtoneId = context.resources.getIdentifier("rices", "raw", "com.drsasimi.houralarm")
//            val ringtoneUrl = Uri.parse("android.resource://com.drsasimi.houralarm/raw/${ringtoneId}")

            val ringtoneUrlStr = prefUtil.getHourSoundUrl(currentHour)
            val ringtoneUrl = if (ringtoneUrlStr.isEmpty()) {
                val ringtoneId = context.resources.getIdentifier("meow", "raw", "com.drsasimi.houralarm")
                Uri.parse("android.resource://com.drsasimi.houralarm/raw/${ringtoneId}")
            } else {
                Uri.parse(ringtoneUrlStr)
            }

            RingtoneUtil.play(context, ringtoneUrl)
        }

        AlarmUtil.execute(context, AlarmUtil.nextHour(1))
    }
}
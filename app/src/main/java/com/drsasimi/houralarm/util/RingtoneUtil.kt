package com.drsasimi.houralarm.util

import android.content.Context
import android.media.AudioManager
import android.media.RingtoneManager
import android.net.Uri

class RingtoneUtil {
    companion object {
        fun play(context: Context, uri: Uri) {
            val ringtone = RingtoneManager.getRingtone(context.applicationContext, uri)
            ringtone.streamType = AudioManager.STREAM_ALARM
            ringtone.play()
        }
    }
}
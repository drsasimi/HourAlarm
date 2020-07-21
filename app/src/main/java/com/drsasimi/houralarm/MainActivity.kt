package com.drsasimi.houralarm

import android.content.DialogInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AlertDialog
import com.drsasimi.houralarm.util.AlarmUtil
import com.drsasimi.houralarm.util.PreferenceUtil
import com.drsasimi.houralarm.util.RingtoneUtil

class MainActivity : AppCompatActivity() {

    private lateinit var prefUtil: PreferenceUtil
    private lateinit var toggleUseAlarm: Switch

    private val toggleHourSwitches: MutableList<Switch> = mutableListOf()
    private val soundHourButtons: MutableList<Button> = mutableListOf()

    // 시간대별 토글 버튼
    private val toggleHourIds = listOf(
        R.id.toggleHour00, R.id.toggleHour01, R.id.toggleHour02, R.id.toggleHour03,
        R.id.toggleHour04, R.id.toggleHour05, R.id.toggleHour06, R.id.toggleHour07,
        R.id.toggleHour08, R.id.toggleHour09, R.id.toggleHour10, R.id.toggleHour11,
        R.id.toggleHour12, R.id.toggleHour13, R.id.toggleHour14, R.id.toggleHour15,
        R.id.toggleHour16, R.id.toggleHour17, R.id.toggleHour18, R.id.toggleHour19,
        R.id.toggleHour20, R.id.toggleHour21, R.id.toggleHour22, R.id.toggleHour23
    )

    // 시간대별 벨소리 변경 버튼
    private val soundHourIds = listOf(
        R.id.btnSndHour00, R.id.btnSndHour01, R.id.btnSndHour02, R.id.btnSndHour03,
        R.id.btnSndHour04, R.id.btnSndHour05, R.id.btnSndHour06, R.id.btnSndHour07,
        R.id.btnSndHour08, R.id.btnSndHour09, R.id.btnSndHour10, R.id.btnSndHour11,
        R.id.btnSndHour12, R.id.btnSndHour13, R.id.btnSndHour14, R.id.btnSndHour15,
        R.id.btnSndHour16, R.id.btnSndHour17, R.id.btnSndHour18, R.id.btnSndHour19,
        R.id.btnSndHour20, R.id.btnSndHour21, R.id.btnSndHour22, R.id.btnSndHour23
    )
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefUtil = PreferenceUtil(this)
        toggleUseAlarm = findViewById(R.id.toggleUseAlarm)
        toggleUseAlarm.setOnClickListener(toggleAlarmListener)

        for ((index, toggleHourId) in toggleHourIds.withIndex()) {
            val toggleHourButton: Switch = findViewById(toggleHourId)
            toggleHourButton.tag = index
            toggleHourButton.setOnClickListener(toggleHourListener)
            toggleHourSwitches.add(toggleHourButton)
        }

        for ((index, toggleHourId) in soundHourIds.withIndex()) {
            val soundHourBtn: Button = findViewById(toggleHourId)
            soundHourBtn.tag = index
            soundHourBtn.setOnClickListener(soundHourListener)
            soundHourButtons.add(soundHourBtn)
        }

        updateUiComponents()
    }

    private val toggleAlarmListener = View.OnClickListener {
        val switchBtn: Switch = it as Switch
        prefUtil.setUseAlarm(switchBtn.isChecked)
        AlarmUtil.destroy(this)

        if (prefUtil.isUseAlarm()) {
            AlarmUtil.execute(this, AlarmUtil.nextHour(1))
        }
    }

    private val toggleHourListener = View.OnClickListener {
        val switchBtn: Switch = it as Switch
        prefUtil.setEnableHour(switchBtn.tag as Int, switchBtn.isChecked)
    }

    private val soundHourListener = View.OnClickListener {
        val button: Button = it as Button
        val index: Int = button.tag as Int
        if (prefUtil.getHourSoundUrl(index).isEmpty()) {
            showChoiceSound(index)
        } else {
            showRemoveSound(index)
        }
    }

    private fun showChoiceSound(hour: Int) {
        var selectedIndex = 0
        var ringtoneNames = arrayOf(
            "meow",
            "bark",
            "classical",
            "coin",
            "nope",
            "rices",
            "surprise",
            "thinking"
        )
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_sound_set_title, hour))
            .setSingleChoiceItems(ringtoneNames, 0) { _, which ->
                selectedIndex = which

                val ringtoneId = resources.getIdentifier(ringtoneNames[which], "raw", "com.drsasimi.houralarm")
                RingtoneUtil.play(this, Uri.parse("android.resource://com.drsasimi.houralarm/raw/${ringtoneId}"))
            }
            .setPositiveButton(R.string.str_ok) { _, _ ->
                Log.w("sound", "Selected $selectedIndex")
                if (selectedIndex > 0) {
                    val ringtoneId = resources.getIdentifier(ringtoneNames[selectedIndex], "raw", "com.drsasimi.houralarm")
                    prefUtil.setHourSoundUrl(hour, "android.resource://com.drsasimi.houralarm/raw/${ringtoneId}")
                } else {
                    prefUtil.removeHourSoundUrl(hour)
                }
                updateUiComponents()
            }
            .setNegativeButton(R.string.str_cancel) { _, _ ->
                prefUtil.removeHourSoundUrl(hour)
                updateUiComponents()
            }
            .show()
    }

    private fun showRemoveSound(hour: Int) {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.dialog_sound_clear_msg, hour))
            .setPositiveButton(R.string.str_ok) { _, _ ->
                prefUtil.removeHourSoundUrl(hour)
                updateUiComponents()
            }
            .setNegativeButton(R.string.str_cancel) { _, _ ->
                updateUiComponents()
            }
            .show()
    }

    private fun updateUiComponents() {
        toggleUseAlarm.isChecked = prefUtil.isUseAlarm()

        for ((index, switchBtn) in toggleHourSwitches.withIndex()) {
            switchBtn.isChecked = prefUtil.isEnableHour(index)
        }

        for ((index, button) in soundHourButtons.withIndex()) {
            if (prefUtil.getHourSoundUrl(index).isEmpty()) {
                button.text = getText(R.string.btn_snd_off)
            } else {
                button.text = getText(R.string.btn_snd_on)
            }
        }
    }
}

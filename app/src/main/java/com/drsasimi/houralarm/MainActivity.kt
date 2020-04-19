package com.drsasimi.houralarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Switch
import com.drsasimi.houralarm.util.AlarmUtil
import com.drsasimi.houralarm.util.PreferenceUtil

class MainActivity : AppCompatActivity() {

    private val toggleHourIds = listOf(
        R.id.toggleHour00, R.id.toggleHour01, R.id.toggleHour02, R.id.toggleHour03,
        R.id.toggleHour04, R.id.toggleHour05, R.id.toggleHour06, R.id.toggleHour07,
        R.id.toggleHour08, R.id.toggleHour09, R.id.toggleHour10, R.id.toggleHour11,
        R.id.toggleHour12, R.id.toggleHour13, R.id.toggleHour14, R.id.toggleHour15,
        R.id.toggleHour16, R.id.toggleHour17, R.id.toggleHour18, R.id.toggleHour19,
        R.id.toggleHour20, R.id.toggleHour21, R.id.toggleHour22, R.id.toggleHour23
    )
    private val toggleHourSwitches: MutableList<Switch> = mutableListOf()

    private lateinit var toggleUseAlarm: Switch
    private lateinit var prefUtil: PreferenceUtil

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

    private fun updateUiComponents() {
        toggleUseAlarm.isChecked = prefUtil.isUseAlarm()
        for ((index, switchBtn) in toggleHourSwitches.withIndex()) {
            switchBtn.isChecked = prefUtil.isEnableHour(index)
        }
    }
}

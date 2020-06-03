package com.example.voctrainer.moduls.settings.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.preference.*
import com.example.voctrainer.R
import com.example.voctrainer.moduls.settings.alarm.AlarmReceiver
import com.example.voctrainer.moduls.settings.dialogs.DialogTimePicker
import com.google.android.material.snackbar.Snackbar
import java.lang.ClassCastException
import java.util.*

class PreferenceFragmentSettings : PreferenceFragmentCompat() {

    // Allgemeine Variablen:
    private lateinit var screen:PreferenceScreen

    // AlarmClock Preferences:
    private var prefSetTime:Preference? = null
    private var prefDayList:MultiSelectListPreference? = null
    private var prefSetAlarm:SwitchPreference? = null


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)

        screen = preferenceScreen

        initAlarmClockPreferences()


    }


    private fun initAlarmClockPreferences()
    {
        // Preference to set the Time of the AlarmClock
        prefSetTime = findPreference("preference_set_alarm_time")
        var key = prefSetTime!!.key
        var value = prefSetTime!!.sharedPreferences.getString(key,"08:00")
        prefSetTime!!.summary = "Eingestellte Zeit: $value"
        var mHour = ""
        var mMinute = ""
        var realHour  = 0
        var realMinute = 0
        if(value != null)
        {
            mHour = value.subSequence(0..1).toString()
            Log.d("VocTrainer","PreferenceFragmentSetting - mHour(pref1) = $mHour")
            mMinute = value.subSequence(3..value.lastIndex).toString()
            Log.d("VocTrainer","PreferenceFragmentSetting - mMinute(pref1) = $mMinute")



            try {
                realHour = Integer.valueOf(mHour)
                realMinute = Integer.valueOf(mMinute)
            } catch (e:ClassCastException)
            {
                e.printStackTrace()
                Log.e("VocTrainer","PreferenceFragmentSetting - failed to cast String to Int")
            }
        }
        if(prefSetTime !=null)
        {
            prefSetTime!!.setOnPreferenceClickListener {

                Log.d("VocTrainer","PreferenceFragmentSetting - value(pref1) = $value")



                var dialog = DialogTimePicker(realHour,realMinute)
                dialog.show(childFragmentManager,"test")
                dialog.setOnTimePickerDialogClick(object:DialogTimePicker.OnTimePickerDialogClick{
                    override fun setOnTimePickerDialogClick(hour: Int, minute: Int)
                    {
                        realHour = hour
                        realMinute = minute

                        var input = ""
                        if (minute<10 && hour < 10)
                        {
                            input = "0${hour}:0${minute}"
                        }
                        else if (minute<10)
                        {
                            input = "${hour}:0${minute}"
                        }
                        else if (hour<10)
                        {
                            input = "0${hour}:${minute}"
                        }

                        else
                        {
                            input = "${hour}:${minute}"
                        }

                        prefSetTime!!.summary = "Eingestellte Zeit: $input"
                        var editor = prefSetTime!!.sharedPreferences.edit()
                        editor.putString(key,input)
                        editor.commit()

                    }

                })


                true
            }
        }


        // Preference to set the days, when the AlarmClock should repeat the alarm:
        prefDayList = findPreference("preference_set_alarm_daylist")
        /*var daySet = prefDayList!!.getPersistedStringSet(setOf())
        if (daySet.isNotEmpty())
        {
            var summary = ""
            for ((index,i) in daySet.withIndex())
            {
                summary += if(index == 0) {
                    "$i"
                } else {
                    " ,$i"
                }

            }
            prefDayList!!.summary = summary
        }
        prefDayList!!.setOnPreferenceChangeListener { preference, newValue ->
            var daySet = prefDayList!!.getPersistedStringSet(setOf())
            if (daySet.isNotEmpty())
            {
                var summary = ""
                for ((index,i) in daySet.withIndex())
                {
                    summary += if(index == 0) {
                        "$i"
                    } else {
                        " ,$i"
                    }

                }
                prefDayList!!.summary = summary
            }
            true
        }*/


        // Preference to Start the AlarmManager:
        fun startAlarmManager()
        {
            var intent = Intent(requireContext(),AlarmReceiver::class.java)
            var pendingIntent = PendingIntent.getBroadcast(requireContext(),0,intent,0)
            var aManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // Calendar:
            // Set the alarm to start at approximately 2:00 p.m.
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, realHour)
                set(Calendar.MINUTE, realMinute)
            }

            Log.d("VocTrainer","Time for AlarmManager = ${calendar.time}")

            aManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)
            Snackbar.make(requireView(),"Alarm wurde gestartet",Snackbar.LENGTH_SHORT).show()

        }
        fun stopAlarmManager()
        {
            var intent = Intent(requireContext(),AlarmReceiver::class.java)
            var pendingIntent = PendingIntent.getBroadcast(requireContext(),0,intent,0)
            var aManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

            aManager.cancel(pendingIntent)

            Snackbar.make(requireView(),"Alarm wurde beendet",Snackbar.LENGTH_SHORT).show()
        }
        prefSetAlarm = findPreference("preference_set_alarm_switch")
        prefSetAlarm!!.setOnPreferenceChangeListener { preference, newValue ->
            if(newValue == true)
            {
                startAlarmManager()
            }
            else
            {
                stopAlarmManager()
            }
            true
        }










    }


}
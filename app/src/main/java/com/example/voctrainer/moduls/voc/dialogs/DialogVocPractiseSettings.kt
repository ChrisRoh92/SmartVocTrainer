package com.example.voctrainer.moduls.voc.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import com.example.voctrainer.backend.database.entities.Setting
import com.example.voctrainer.moduls.voc.adapter.VocPractiseSettingsRecyclerViewAdapter

class DialogVocPractiseSettings(var currentSetting: Setting):DialogFragment()
{


    // Allgemeine Variablen:
    private lateinit var dialogView: View
    private lateinit var mListener:OnDialogClickListener
    private var newSetting: Setting? = null

    // Items of the newSetting:
    private var newItemCount = currentSetting.itemCount
    private var newTimeMode = currentSetting.timeMode
    private var newTime = currentSetting.time
    private var newPractiseMode = currentSetting.practiseMod
    private var newSettingsMod = currentSetting.settingsMode



    // View Elemente:
    private lateinit var btnSave:Button
    private lateinit var btnAbort:Button

    // RecyclierView
    private lateinit var rv:RecyclerView
    private lateinit var layoutManager:LinearLayoutManager
    private lateinit var adapter:VocPractiseSettingsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog)

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        dialogView = inflater.inflate(R.layout.dialog_voc_practise_settings, container, false)

        // Init Buttons:
        initButtons()
        // InitRecyclerView:
        initRecyclerView()


        return dialogView
    }

    private fun initButtons()
    {
        btnSave = dialogView.findViewById(R.id.dialog_voc_practise_settings_btn_save)
        btnAbort = dialogView.findViewById(R.id.dialog_voc_practise_settings_btn_abort)

        btnSave.setOnClickListener {
            newSetting = Setting(0L,currentSetting.bookId,newItemCount,newTimeMode,newTime,newPractiseMode,newSettingsMod)
            if(newSetting!= currentSetting)
            {
                mListener?.setOnDialogClickListener(newSetting!!)
                dismiss()
            }
            else
            {
                Toast.makeText(requireContext(),"Es wurden keine Änderungen durchgeführt!",Toast.LENGTH_SHORT).show()
            }
        }
        btnAbort.setOnClickListener { dismiss() }
    }

    private fun initRecyclerView()
    {
        rv = dialogView.findViewById(R.id.dialog_voc_practise_settings_rv)
        layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        adapter = VocPractiseSettingsRecyclerViewAdapter(currentSetting.timeMode,currentSetting.practiseMod)
        rv.layoutManager = layoutManager
        rv.setHasFixedSize(true)
        rv.adapter = adapter
        updateSubText()

        // ClickListener:
        adapter.setOnItemClickListener(object :VocPractiseSettingsRecyclerViewAdapter.OnItemClickListener{
            override fun setOnItemClickListener(position: Int,checked:Boolean) {
                // 0: Anzahl Vokabeln
                // 1: Mit oder ohne Zeitbeschränkung
                // 2: Zeitbeschränkung angeben:
                // 3: Übungsstatus der Vokabeln festlegen
                // 4: Bewertung zwischendurch oder am Ende?
                performItemClick(position,checked)


            }

        })
    }

    private fun performItemClick(pos:Int,checked:Boolean)
    {
        // Anzahl Vokabeln setzen:
        fun setNumberVocs()
        {
            val dialog = DialogVocPractiseItemCount(newItemCount)
            dialog.show(parentFragmentManager,"Number of items:")
            dialog.setOnItemCountClickListener(object :DialogVocPractiseItemCount.OnItemCountClickListener{
                override fun setOnItemCountClickListener(itemCount: Int) {
                    newItemCount = itemCount
                    //TODO("set the New ItemCount into the field in the RecyclerView")
                    updateSubText()

                }

            })
        }

        fun setTime()
        {
            // Calc Time from Seconds
            fun calcTime():Array<Int>
            {
                var time = newTime
                var hour = 0
                var minute = 0
                var sec = 0

                hour = (time/3600.0).toInt()
                time -= hour*3600

                minute = (time/60.0).toInt()
                time -= minute*60

                sec = time.toInt()

                return arrayOf(hour,minute,sec)


            }
            val time = calcTime()
            val dialog = DialogVocPractiseTime(time[0],time[1],time[2])
            dialog.show(parentFragmentManager,"Time:")
            dialog.setOnTimeClickListener(object : DialogVocPractiseTime.OnTimeClickListener{
                override fun setOnTimeClickListener(hour: Int, minute: Int, second: Int) {
                    newTime = (3600*hour+60*minute+second).toLong()
                    updateSubText()
                    //TODO("set the New Time into the field in the RecyclerView")
                }

            })


        }

        fun setPractiseMode()
        {
            val dialog = DialogVocPractiseSettingsMode(newSettingsMod)
            dialog.show(parentFragmentManager,"Set SettingsMod")
            dialog.setOnSettingsModeClickListener(object:DialogVocPractiseSettingsMode.OnSettingsModeClickListener{
                override fun setOnSettingsModeClickListener(settingsMode: Int) {
                    newSettingsMod = settingsMode
                    updateSubText()
                    //TODO("set the New newSettingsMod into the field in the RecyclerView")
                }

            })
        }

        when(pos)
        {
            0 -> setNumberVocs()
            1 -> newTimeMode = checked
            2 -> setTime()
            3 -> setPractiseMode()
            4 -> newPractiseMode = checked
        }



    }

    private fun updateSubText()
    {

        newSetting = Setting(0L,currentSetting.bookId,newItemCount,newTimeMode,newTime,newPractiseMode,newSettingsMod)
        fun createSubTitles():ArrayList<String>
        {

            fun calcTime():Array<Int>
            {
                var time = newTime
                var hour = 0
                var minute = 0
                var sec = 0

                hour = (time/3600.0).toInt()
                time -= hour*3600

                minute = (time/60.0).toInt()
                time -= minute*60

                sec = time.toInt()

                return arrayOf(hour,minute,sec)


            }
            val timeValues = calcTime()

            var values = ArrayList<String>()
            var time:String = if(newSetting!!.timeMode) "Aktiviert" else "Deaktiviert"
            var practiseMode = arrayListOf("Nur Ungeübte Vokabeln","Nur Vokabeln in Übung","Ungeübte und in Übung","Alle Arten")
            var evaluation =if(newSetting!!.practiseMod) "Sofort" else "Am Ende"
            var items = if(newSetting!!.itemCount != -1) "${newSetting!!.itemCount}" else "Alle Vokabeln"


            values.add("Ausgewählte Anzahl: $items")
            values.add("Zeitbeschränkung aktivieren?")
            values.add("Eingestellte Zeit: ${timeValues[0]} Std. ${timeValues[1]} Min. ${timeValues[2]} Sek.")
            values.add("Übungsstatus Vokabeln: ${practiseMode[newSetting!!.settingsMode]}")
            values.add("Eingaben werden erst am Schluss bewerte")


            return values
        }

        adapter.updateSubs(createSubTitles(),newTimeMode,newPractiseMode)
    }




    // Interface:
    interface OnDialogClickListener
    {
        fun setOnDialogClickListener(newSetting:Setting)
    }

    fun OnDialogClickListener(mListener:OnDialogClickListener)
    {
        this.mListener = mListener
    }


}
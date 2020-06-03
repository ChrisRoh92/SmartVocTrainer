package com.example.voctrainer.moduls.settings.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.voctrainer.R
import com.example.voctrainer.moduls.settings.adapter.SettingsRecyclerViewAdapter


class SettingsFragment : Fragment() {

    // Allgemeine Variablen:
    private lateinit var rootView:View
    private var source = 0 // 0 von Main und 1 von Voc

    // Toolbar
    private lateinit var toolbar: Toolbar

    // RecyclerView Stuff
    // Allgemeine Einstellungen
    private lateinit var rvSetting:RecyclerView
    private lateinit var layoutManagerSetting:LinearLayoutManager
    private lateinit var adapterSetting:SettingsRecyclerViewAdapter
    // Share
    private lateinit var rvShare:RecyclerView
    private lateinit var layoutManagerShare:LinearLayoutManager
    private lateinit var adapterShare:SettingsRecyclerViewAdapter
    // Privacy
    private lateinit var rvPrivacy:RecyclerView
    private lateinit var layoutManagerPrivacy:LinearLayoutManager
    private lateinit var adapterPrivacy:SettingsRecyclerViewAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_settings_old, container, false)
        source = arguments?.getInt("source")!!
        initToolBar()
        initRecyclerView()



        return rootView
    }


    private fun initToolBar()
    {
        toolbar = rootView.findViewById(R.id.fragment_settings_toolbar)


    }

    private fun initRecyclerView()
    {
        // Settings RV:
        rvSetting = rootView.findViewById(R.id.fragment_setting_rv_settings)
        layoutManagerSetting = LinearLayoutManager(rootView.context,RecyclerView.VERTICAL,false)
        adapterSetting = SettingsRecyclerViewAdapter(arrayListOf("Systemsprache einstellen","App Daten zurücksetzen"),
            arrayListOf("Hier hast du die Möglichkeit die Systemsprache einzustellen","Hier hast du die Möglichkeit die Daten (Vokabelhefte etc.) zurückzusetzen"))
        rvSetting.layoutManager = layoutManagerSetting
        rvSetting.adapter = adapterSetting
        adapterSetting.setOnItemClickListener(object:SettingsRecyclerViewAdapter.OnItemClickListener{
            override fun setOnItemClickListener(pos: Int) {

            }

        })

        // Share RV:
        rvShare = rootView.findViewById(R.id.fragment_setting_rv_share)
        layoutManagerShare = LinearLayoutManager(rootView.context,RecyclerView.VERTICAL,false)
        adapterShare = SettingsRecyclerViewAdapter(arrayListOf("Link zum App Store Teilen","App im Store bewerten"),
            arrayListOf("Durch einen Klick hier, hast du die Möglichkeit einen Link zur App über alle gängigen Medien zu Teilen","Hier kannst du die App bewerten"))
        rvShare.layoutManager = layoutManagerShare
        rvShare.adapter = adapterShare
        adapterShare.setOnItemClickListener(object:SettingsRecyclerViewAdapter.OnItemClickListener{
            override fun setOnItemClickListener(pos: Int) {

            }

        })

        // Privacy RV:
        rvPrivacy = rootView.findViewById(R.id.fragment_setting_rv_privacy)
        layoutManagerPrivacy = LinearLayoutManager(rootView.context,RecyclerView.VERTICAL,false)
        adapterPrivacy = SettingsRecyclerViewAdapter(arrayListOf("Datenschutzerklärung anzeigen","Verwendete Lizenzen anzeigen"),
            arrayListOf("Hier hast du die Möglichkeit dir die Datenschutzerklärung anzuzeigen","Hier hast du die Möglichkeit die in dieser App verwendeten Lizenzen dir anzeigen zu lassen"))
        rvPrivacy.layoutManager = layoutManagerPrivacy
        rvPrivacy.adapter = adapterPrivacy
        adapterPrivacy.setOnItemClickListener(object:SettingsRecyclerViewAdapter.OnItemClickListener{
            override fun setOnItemClickListener(pos: Int) {

            }

        })
    }


}

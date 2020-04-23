package com.example.voctrainer.moduls.intro.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.voctrainer.R
import com.example.voctrainer.moduls.intro.adapter.ViewPager2Adapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * A simple [Fragment] subclass.
 */
class IntroFragment : Fragment() {

    // Allgemeines Stuff:
    private lateinit var rootView:View

    // Daten für die Slides:
    private var titles:ArrayList<String> = ArrayList()
    private var subTitles:ArrayList<String> = ArrayList()
    private var images:ArrayList<Int> = ArrayList()

    // View Elemente:
    private lateinit var btn: Button
    private lateinit var tabLayout:TabLayout

    // ViewPager2 Stuff:
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter:ViewPager2Adapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_intro, container, false)


        initContent()
        initViewPager2()
        initButton()


        return rootView
    }

    private fun initButton()
    {
        btn = rootView.findViewById(R.id.fragment_intro_btn_next)
        fun scrollToItem()
        {
            val currentPosition = viewPager2.currentItem
            if(currentPosition < titles.lastIndex)
            {
                viewPager2.setCurrentItem(currentPosition+1,true)
            }

        }
        btn.setOnClickListener {
            if(viewPager2.currentItem < titles.lastIndex)
            {
                scrollToItem()
            }
            else
            {
                findNavController().navigate(R.id.action_intro_main)
            }

        }
    }

    private fun initViewPager2()
    {
        viewPager2 = rootView.findViewById(R.id.fragment_intro_viewpager2)
        adapter = ViewPager2Adapter(titles,subTitles,images)
        viewPager2.adapter = adapter

        // Tablayout anbinden
        tabLayout = rootView.findViewById(R.id.fragment_intro_tablayout)
        TabLayoutMediator(tabLayout,viewPager2){tab,position ->
        }.attach()

        viewPager2.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position == titles.lastIndex)
                {
                    btn.text = "App Starten"
                }
                else
                {
                    btn.text = "Next"
                }
            }
        })
    }

    private fun initContent()
    {
        titles.add("Erfolgreiches Üben")
        titles.add("Teilen von Vokabeln")
        titles.add("Nachhaltiger Lernerfolg")

        subTitles.add("Verschiedene Testmodi zum abfragen deiner Vokabeln steher dir zur Verfügung. So bist auf den nächsten Test bestens gewappnet!")
        subTitles.add("Keine Lust die Vokabeln einzutragen? Bitte deine Freunde dir ihre Vokabelhefte zu schicken. VocTrainer unterstützt das automatische einlesen")
        subTitles.add("Wieder mal vergessen Vokabeln zu lernen? VocTrainer erinnert dich regelmäßig deine Vokabeln nochmal anzuschauen.")

        images.add(R.drawable.ic_launcher_foreground)
        images.add(R.drawable.ic_launcher_foreground)
        images.add(R.drawable.ic_launcher_foreground)
    }



}

package com.example.voctrainer.frontend.voc.adapter

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.voctrainer.frontend.voc.fragment.VocDatasFragment
import com.example.voctrainer.frontend.voc.fragment.VocHomeFragment
import com.example.voctrainer.frontend.voc.fragment.VocPractiseFragment
import com.example.voctrainer.frontend.voc.fragment.VocStatisticFragment

class VocStateAdapter(var fragment: Fragment) : FragmentStateAdapter(fragment)
{

    private val fragments:ArrayList<Fragment> = arrayListOf(VocHomeFragment(),VocDatasFragment(),VocPractiseFragment(),VocStatisticFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }



}
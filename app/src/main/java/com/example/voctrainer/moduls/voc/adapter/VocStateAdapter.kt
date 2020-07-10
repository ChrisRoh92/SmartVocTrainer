package com.example.voctrainer.moduls.voc.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.voctrainer.moduls.voc.fragment.VocDatasFragment
import com.example.voctrainer.moduls.voc.fragment.VocHomeFragment
import com.example.voctrainer.moduls.voc.fragment.VocPractiseFragment
import com.example.voctrainer.moduls.voc.fragment.VocStatisticFragment

class VocStateAdapter(var fragment: Fragment,val bookId:Long) : FragmentStateAdapter(fragment)
{

    private val fragments:ArrayList<Fragment> = arrayListOf(VocDatasFragment(),VocPractiseFragment(),VocStatisticFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        var fragment = fragments[position]
        var bundle = Bundle()
        bundle.putLong("bookId",bookId)
        fragment.arguments = bundle

        return fragment
    }



}
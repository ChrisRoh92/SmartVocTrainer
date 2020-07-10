package com.example.voctrainer.moduls.intro.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.voctrainer.R
import kotlinx.coroutines.*


class SplashFragment : Fragment() {

    private lateinit var rootView: View

    // Coroutine Stuff:
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        // Prüfen, ob schonmal gestartet wurde...

        rootView = inflater.inflate(R.layout.fragment_splash, container, false)
        uiScope.launch {
            startAnimation()
        }

        return rootView
    }

    private suspend fun startAnimation()
    {
        delay(500)
        findNavController().navigate(R.id.action_splash_main)

    }

}

package com.example.voctrainer.moduls.practise.fragments

import android.animation.Animator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.fragment.findNavController
import com.example.voctrainer.R
import com.example.voctrainer.moduls.standard.dialogs.DialogStandardAlert
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*

class PractiseFragment():Fragment()
{

    // Allgemeine Variablen:
    private lateinit var rootView: View

    // Coroutine Stuff:
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)

    // View Elemente:
    // Texts and EditTexts:
    private lateinit var etInput:TextInputLayout
    private lateinit var tvVoc:TextView
    // Colors:
    private lateinit var colors:ArrayList<Int>

    // Buttons:
    private lateinit var btnOk:Button
    private lateinit var btnAbort:Button
    private lateinit var btnNext:ImageButton
    private lateinit var btnBack:ImageButton

    // Countdown:
    private lateinit var cardviewCountdown:CardView
    private lateinit var tvCountdown: TextView
    private var hour=0
    private var minute=0
    private var second=15


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_practise, container, false)
        // TODO im Dialog ein interface implementieren, dass auslöst sobald der Countdown abgelaufen ist
        /*var dialog = PractiseCountdownDialog()
        dialog.show(parentFragmentManager,"Countdown")*/
        initButtons()
        initViews()

        initCountDown()


        // PractiseViewModel einfügen
        // Übergeben über Args. wird die Id der Settings...

        return rootView
    }

    private fun initViews()
    {
        // InitColors:
        colors = ArrayList()
        colors.add(ContextCompat.getColor(requireContext(),R.color.design_default_color_error))
        colors.add(ContextCompat.getColor(requireContext(),R.color.colorAccent))
        colors.add(ContextCompat.getColor(requireContext(),R.color.text_main))

        tvVoc = rootView.findViewById(R.id.fragment_practise_tv_voc)
        etInput = rootView.findViewById(R.id.fragment_practise_et)


        etInput.editText!!.setOnEditorActionListener(object:TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                var handled = false
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    // Enter entspricht dem klick auf den btnOK
                    btnOk.performClick()

                    handled = true
                }
                return handled
            }

        })



    }

    private fun initCountDown()
    {
        cardviewCountdown = rootView.findViewById(R.id.fragment_practise_cardview_time)
        tvCountdown = rootView.findViewById(R.id.fragment_practise_tv_countdown)

        cardviewCountdown.visibility = View.VISIBLE
        fun createTimeString():String
        {
            var sHour = ""
            var sMinute = ""
            var sSecond = ""
            sHour = if(hour > 9)
                "$hour"
            else
                "0$hour"

            sMinute = if(minute > 9)
                "$minute"
            else
                "0$minute"

            sSecond = if(second > 9)
                "$second"
            else
                "0$second"

            return "${sHour}h ${sMinute}m ${sSecond}s"

        }
        tvCountdown.text = createTimeString()
        uiScope.launch {
            while(hour+minute+second != 0)
            {
                delay(1000)
                // Sekunde um 1 reduzieren
                // Wenn Anzahl Sekunden größer null
                if(second>0)
                {
                    second--
                }
                // Wenn nicht dann...
                else
                {
                    // Auf 59 Sekunden stellen, sofern
                    // Stunde und minute > 0
                    if(hour+minute>0)
                    {
                        second = 59
                        // Minute um 1 reduzieren:
                        // Wenn Minute größer 0
                        if(minute > 0)
                        {
                            minute--
                        }
                        // Wenn nicht dann
                        else
                        {
                            // Auf 59 Minuten stellen, sofern hour > 0
                            if(hour>0)
                            {
                                minute = 59
                                hour--
                                // Stunde um 1 reduzieren:

                            }
                        }
                    }
                    else
                    {
                        // Alles ist fertig
                        // Wird eigentlich über die While Schleife abgefangen
                    }
                }
                tvCountdown.text = createTimeString()


            }
            findNavController().navigate(R.id.action_practise_result)
        }
    }


    private fun initButtons()
    {
        btnOk = rootView.findViewById(R.id.fragment_practise_btn_ok)
        btnAbort = rootView.findViewById(R.id.fragment_practise_btn_abort)
//        btnNext = rootView.findViewById(R.id.fragment_practise_btn_next)
//        btnBack = rootView.findViewById(R.id.fragment_practise_btn_back)

        // Listener:
        btnOk.setOnClickListener {
            if(etInput.editText!!.text.isNotEmpty())
            {
                // TODO() : Je nachdem wie die Settings sind, wird die Methode aufgerufen
                checkForResult()
                etInput.editText!!.text.clear()
            }


        }



        btnAbort.setOnClickListener {
            var dialog = DialogStandardAlert("Möchten Sie den Versuch wirklich abbrechen?","Dieser Vorgang kann nicht rückgängig gemacht werden")
            dialog.show(childFragmentManager,"")
            dialog.setOnDialogClickListener(object:DialogStandardAlert.OnDialogClickListener{
                override fun setOnDialogClickListener() {
                    findNavController().navigate(R.id.action_practise_result)
                }

            })
        }
//        btnNext.setOnClickListener {  }
//        btnBack.setOnClickListener {  }
    }


    private fun checkForResult()
    {
        var extract = etInput.editText!!.text.toString()
        // TODO(): Eingabe muss dann noch dem ViewModel übergeben werden...
        if(extract == "correct")
        {
            uiScope.launch {


                animateVocText("Korrekte Eingabe",colors[1]){
                    uiScope.launch {
                        delay(500)
                        animateVocText("Neue Vokabel",colors[2]){
                        }


                    }
                }

            }
        }
        else
        {
            uiScope.launch {


                animateVocText("Falsche Eingabe", colors[0]){
                    uiScope.launch {
                        delay(500)
                        animateVocText("Neue Vokabel",colors[2]){
                    }


                    }
                }

            }
        }
    }

    private fun animateVocText(input:String,color:Int,callback:()->Unit)
    {

        tvVoc.animate()
            .alpha(0f)
            .scaleY(0f)
            .scaleX(0f)
            .setDuration(250).setInterpolator(FastOutSlowInInterpolator())
            .setListener(object: Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    tvVoc.text = input
                    tvVoc.setTextColor(color)
                    tvVoc.animate()
                        .alpha(1f)
                        .scaleY(1f)
                        .scaleX(1f)
                        .setDuration(250).setInterpolator(FastOutSlowInInterpolator())
                        .setListener(object: Animator.AnimatorListener{
                            override fun onAnimationRepeat(animation: Animator?) {

                            }

                            override fun onAnimationEnd(animation: Animator?) {
                                callback()
                            }

                            override fun onAnimationCancel(animation: Animator?) {

                            }

                            override fun onAnimationStart(animation: Animator?) {

                            }

                        })
                        .start()

                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationStart(animation: Animator?) {

                }

            })
            .start()
        tvVoc.clearAnimation()

    }

}
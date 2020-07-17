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
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.voctrainer.R
import com.example.voctrainer.animateVocText
import com.example.voctrainer.backend.database.entities.Setting
import com.example.voctrainer.backend.database.entities.Voc
import com.example.voctrainer.moduls.main.viewmodel.MainViewModel
import com.example.voctrainer.moduls.practise.dialog.PractiseCountdownDialog
import com.example.voctrainer.moduls.practise.viewmodel.PractiseViewModel
import com.example.voctrainer.moduls.practise.viewmodel.PractiseViewModelFactory
import com.example.voctrainer.moduls.standard.dialogs.DialogStandardAlert
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*
import kotlin.math.roundToInt

class PractiseFragment():Fragment()
{

    // Allgemeine Variablen:
    private lateinit var rootView: View
    private val TAG = "VocTrainer"
    private val duration = 3

    // ViewModel
    private lateinit var viewModel:PractiseViewModel
    private lateinit var viewModelFactory:PractiseViewModelFactory

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

    // Progress:
    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgress:TextView

    // Countdown:
    private lateinit var cardviewCountdown:CardView
    private lateinit var tvCountdown: TextView
    private var hour=0
    private var minute=0
    private var second=2

    // BundleInformations:
    private var bookID = 0L
    private var settingsID = 0L

    // Local Data:
    private var index = 0
    private var testVocs:ArrayList<Voc> = ArrayList()
    private var setting: Setting? = null
    private var settingsLoaded = false
    private var vocsLoaded = false


    // Auswertung:
    private var correctSolutions:ArrayList<Int> = ArrayList()
    private var solution:ArrayList<String> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_practise, container, false)
        // TODO im Dialog ein interface implementieren, dass auslöst sobald der Countdown abgelaufen ist
        /**/
        bookID = requireArguments().getLong("bookID",-1)
        settingsID = requireArguments().getLong("settingsID",-1)

        Log.d(TAG,"PractiseFragment bookID $bookID - settingsID $settingsID")
        viewModelFactory = PractiseViewModelFactory(bookID,settingsID,requireActivity().application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(PractiseViewModel::class.java)


        initObserver()
        initButtons()
        initViews()

        // Handle the OnBackButtonPress!
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                var dialog = DialogStandardAlert("Möchten Sie den Versuch wirklich abbrechen?","Dieser Vorgang kann nicht rückgängig gemacht werden")
                dialog.show(parentFragmentManager,"")
                dialog.setOnDialogClickListener(object: DialogStandardAlert.OnDialogClickListener{
                    override fun setOnDialogClickListener() {
                        findNavController().navigateUp()
                    }

                })


            }

        })


        return rootView
    }

    private fun initObserver()
    {
        // Get Info if currentSettings are loaded
        viewModel.getCurrentSetting().observe(viewLifecycleOwner, Observer {
            settingsLoaded = true
            setting = it
            Log.d(TAG,"PractiseFragment vocLoaded? $vocsLoaded - settingsLoaded? $vocsLoaded")
            if(settingsLoaded && vocsLoaded)
            {
                Log.d(TAG,"PractiseFragment vocLoaded? $vocsLoaded - settingsLoaded? $vocsLoaded - start createVocSet()")
                viewModel.createVocSet()
            }
        })

        // Get Info, if VocList from Book is loaded...
        viewModel.getVocList().observe(viewLifecycleOwner, Observer {
            vocsLoaded = true
            Log.d(TAG,"PractiseFragment vocLoaded? $vocsLoaded - settingsLoaded? $vocsLoaded")
            if(settingsLoaded && vocsLoaded)
            {
                Log.d(TAG,"PractiseFragment vocLoaded? $vocsLoaded - settingsLoaded? $vocsLoaded - start createVocSet()")
                viewModel.createVocSet()
            }
        })

        // Observer, wenn Liste zum Abfragen bereit, Countdown starten...
        viewModel.getTestVocList().observe(viewLifecycleOwner, Observer { vocs ->
            var dialog = PractiseCountdownDialog(duration)
            dialog.show(parentFragmentManager,"Countdown")
            dialog.setOnReadyListener(object:PractiseCountdownDialog.OnReadyListener{
                override fun setOnReadyListener() {
                    dialog.dismiss()
                    testVocs = ArrayList(vocs)
                    Log.d(TAG,"PractiseFragment setting!!.timeMode = ${setting!!.timeMode}")
                    progressBar.max = testVocs.size
                    updateProgress()
                    if(setting!!.timeMode)
                    {
                        initCountDown()

                    }
                    else
                    {
                        startTestSequence()
                    }
                }

            })


        })

        // Observer, if Test was created succesfull:
        viewModel.getReadyForResult().observe(viewLifecycleOwner, Observer {
            if(index > 0)
            {
                var bundle = Bundle()
                bundle.putLong("bookID",bookID)
                bundle.putLong("testID",it.last().id)
                bundle.putLong("settingsID",it.last().settingsID)
                Log.d(TAG,"VocPractiseFragment bookID ${bookID} - testID $it.last().id")
                Log.d(TAG,"PractiseFragment Test = ${it.last()}")
                findNavController().navigate(R.id.action_practise_result,bundle)
            }
        })
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


        // ProgressBar
        progressBar = rootView.findViewById(R.id.fragment_practise_pb_progress)
        tvProgress = rootView.findViewById(R.id.fragment_practise_tv_progress)
        tvProgress.text = ""
        progressBar.progress = 0


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
            startTestSequence()
        }
    }


    private fun startTestSequence()
    {
        if(index < testVocs.size)
        {
            uiScope.launch {
                delay(500)
                animateVocText(tvVoc,testVocs[index].native2,colors[2]){
                    updateProgress()
                    index++
                }

            }



        }
        else
        {
            uiScope.launch {
                delay(500)
                animateVocText(tvVoc,"Test Beendet",colors[2]){
                    uiScope.launch {
                        delay(500)
                        updateProgress()
                        viewModel.createNewTest(solution,correctSolutions)
                    }
                }



            }

        }



    }

    private fun checkForResult()
    {
        var extract = etInput.editText!!.text.toString()
        solution.add(extract)
        // TODO(): Eingabe muss dann noch dem ViewModel übergeben werden...
        if(extract == testVocs[index-1].foreign)
        {
            correctSolutions.add(1)
            uiScope.launch {


                animateVocText(tvVoc,"Korrekte Eingabe",colors[1]){
                    startTestSequence()
                }

            }
        }
        else
        {
            correctSolutions.add(0)
            uiScope.launch {


                animateVocText(tvVoc,"Falsche Eingabe", colors[0]){
                    uiScope.launch {
                        delay(500)
                        animateVocText(tvVoc,testVocs[index-1].foreign,colors[0]){
                            startTestSequence()

                        }


                    }
                }

            }
        }
    }




    private fun updateProgress()
    {
        progressBar.progress = index
        var progress = (((index)/(testVocs.size.toFloat()))*100).roundToInt()



        animateVocText(tvProgress,"$progress %",colors[1]){}
        //tvProgress.text = "$progress %"
    }



}
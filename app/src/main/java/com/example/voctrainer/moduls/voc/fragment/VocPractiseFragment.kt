package com.example.voctrainer.moduls.voc.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import com.example.voctrainer.backend.database.entities.Setting
import com.example.voctrainer.moduls.default_elements.adapter.Simple2RecyclerViewAdapter
import com.example.voctrainer.moduls.standard.dialogs.DialogStandardAlert
import com.example.voctrainer.moduls.voc.adapter.VocPractiseResultRecyclerViewAdapter
import com.example.voctrainer.moduls.voc.dialogs.DialogVocPractiseSettings
import com.example.voctrainer.moduls.voc.viewmodel.VocViewModel
import com.example.voctrainer.moduls.voc.viewmodel.VocViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Exception



/*
****** Offene Tasks ******
- TODO() Es müssen noch Übungseinstellungen definiert werden, die dargestellt werden müssen
- TODO() Sortieren und Filtern von Tests muss noch implementiert werden... (Alles in Dao)
******************************************************************
 */
class VocPractiseFragment: Fragment()
{
    // Allgemeine Variablen:
    private lateinit var rootView: View
    private val TAG = "VocTrainer"

    // View Elemente:
    private lateinit var fabStart:FloatingActionButton
    private lateinit var btnSettings:ImageButton
    private lateinit var btnSort:ImageButton
    private lateinit var btnFilter:ImageButton


    // RecyclerView for Practise Results::
    private lateinit var rvResults:RecyclerView
    private lateinit var managerResult:LinearLayoutManager
    private lateinit var adapterResult:VocPractiseResultRecyclerViewAdapter

    // RecyclerView for Practise Settings:
    private lateinit var rvSettings:RecyclerView
    private lateinit var managerSettings:LinearLayoutManager
    private lateinit var adapterSettings: Simple2RecyclerViewAdapter


    // ViewModel:
    private lateinit var vocViewModel: VocViewModel
    private lateinit var vocViewModelFactory: VocViewModelFactory
    private var bookId:Long = 0
    private var currentSetting: Setting? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)




    }

    private fun startObserver()
    {
        vocViewModel.tests.observe(viewLifecycleOwner, Observer { tests ->
            adapterResult.setNewContent(ArrayList(tests))
            Log.e("VocTrainer","$tests")
        })



        vocViewModel.getSettings()?.observe(viewLifecycleOwner, Observer { settings ->
            if(!settings.isNullOrEmpty())
            {
                currentSetting = settings.last()
                Log.d(TAG,"Anzahl Elemente in aktuellen Settings_List = ${settings.size}")
                Log.d(TAG,"Aktuelles Setting: in aktuellen Settings_List = $currentSetting")

                fun createSubTitles():ArrayList<String>
                {

                    fun calcTime():Array<Int>
                    {
                        var time = currentSetting!!.time
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
                    var time:String = if(currentSetting!!.timeMode) "Aktiviert" else "Deaktiviert"
                    var practiseMode = arrayListOf("Nur Ungeübte Vokabeln","Nur Vokabeln in Übung","Ungeübte und in Übung","Alle Arten")
                    var evaluation =if(currentSetting!!.practiseMod) "Sofort" else "Am Ende"
                    var items = if(currentSetting!!.itemCount != -1) "${currentSetting!!.itemCount}" else "Alle Vokabeln"


                    values.add("Ausgewählte Anzahl: $items")
                    values.add("Status: $time")
                    values.add("Eingestellte Zeit: ${timeValues[0]} Std. ${timeValues[1]} Min. ${timeValues[2]} Sek.")
                    values.add("Übungsstatus Vokabeln: ${practiseMode[currentSetting!!.settingsMode]}")
                    values.add("Bewertungszeitpunkt: $evaluation")


                    return values
                }


                adapterSettings.updateSubContent(createSubTitles())
            }
            else
            {

                vocViewModel.createDefaultSettings()

            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_voc_practise, container, false)

        initFabButton()
        initViews()
        initRecyclerView()
        initSettingsRecyclerView()

        bookId = requireArguments()?.getLong("bookId",0)
        try {
            vocViewModelFactory = VocViewModelFactory(bookId!!,requireActivity().application)
            vocViewModel = ViewModelProvider(requireParentFragment(),vocViewModelFactory).get(VocViewModel::class.java)
            startObserver()
        } catch (e: Exception)
        {
            e.printStackTrace()

        }

        return rootView
    }

    private fun initFabButton()
    {
        // FloatingActionButton:
        fabStart = rootView.findViewById(R.id.fragment_voc_practise_fab)
        fabStart.setOnClickListener {

            fun startNewPractiseTest()
            {
                var bundle = Bundle()
                //Log.d("VocLearner","MainFragment - initAdapterister - adapter.setOnAdapterShowButtonClick: bookdId = $bookId")
                bundle.putLong("bookID",vocViewModel.bookId)
                bundle.putLong("settingsID",currentSetting!!.id)
                Log.d(TAG,"VocPractiseFragment bookID ${vocViewModel.bookId} - settingsID $currentSetting")
                findNavController().navigate(R.id.action_voc_practise,bundle)
            }

            // Prüfen ob Genug Vokabeln vorhanden sind:
            if(vocViewModel.getVocs().value!!.isNullOrEmpty())
            {
                Toast.makeText(requireContext(),"Es sind noch keine Vokabeln eingetragen",Toast.LENGTH_SHORT).show()
            }

            else if(currentSetting!!.itemCount <= vocViewModel.getVocs().value!!.size)
            {
                startNewPractiseTest()
            }
            else
            {
                var dialog = DialogStandardAlert("Achtung, nicht genug Vokabeln verfügbar","Es wird stattdessen alle Vokabeln geprüft!")
                dialog.show(parentFragmentManager,"")
                dialog.setOnDialogClickListener(object: DialogStandardAlert.OnDialogClickListener{
                    override fun setOnDialogClickListener() {
                        startNewPractiseTest()
                    }

                })
            }



        }
    }

    private fun initViews()
    {

        // ImageButtons:
        btnSettings = rootView.findViewById(R.id.fragment_voc_practise_btn_settings)
        btnFilter = rootView.findViewById(R.id.fragment_voc_practise_btn_filter)
        btnSort = rootView.findViewById(R.id.fragment_voc_practise_btn_sort)

        btnSettings.setOnClickListener { startPractiseSettingsDialog() }

    }

    private fun initRecyclerView()
    {
        rvResults = rootView.findViewById(R.id.fragment_voc_practise_rv_result)
        managerResult = LinearLayoutManager(rootView.context,RecyclerView.VERTICAL,false)
        adapterResult = VocPractiseResultRecyclerViewAdapter(ArrayList())
        rvResults.layoutManager = managerResult
        rvResults.adapter = adapterResult
        adapterResult.setOnItemClickListener(object:VocPractiseResultRecyclerViewAdapter.OnItemClickListener{
            override fun setOnItemClickListener(pos: Int) {
                findNavController().navigate(R.id.action_global_result)
            }
        })

        adapterResult.setOnRepeatClickListener(object:VocPractiseResultRecyclerViewAdapter.OnRepeatClickListener{
            override fun setOnRepeatClickListener(pos: Int) {
                // Einstellungen für Test nehmen
                // TODO(): Schnittstelle in PRactise bereitstellen um PractiseSettings entgegen zu nehmen
                // findNavController().navigate(R.id.action_voc_practise)
            }

        })



    }

    private fun initSettingsRecyclerView()
    {
        // TODO() - Darf erst aufgerufen werden, wenn die aktuelle Settings verfügbar ist --> Observer
        rvSettings = rootView.findViewById(R.id.fragment_voc_practise_rv_settings)
        managerSettings = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        // Data:
        val titles:ArrayList<String> = arrayListOf(
            "Anzahl Vokabeln",
            "Mit Zeit Beschränkung",
            "Zeit Beschränkung",
            "Übungsstatus Vokabeln",
            "Bewertung am Ende")


        adapterSettings =
            Simple2RecyclerViewAdapter(
                titles,
                ArrayList(List(titles.size) { "" }))
        rvSettings.setHasFixedSize(true)
        rvSettings.layoutManager = managerSettings
        rvSettings.adapter = adapterSettings


        adapterSettings.setOnItemClickListener(object :Simple2RecyclerViewAdapter.OnItemClickListener{
            override fun setOnItemClickListener(pos: Int) {
                startPractiseSettingsDialog()
            }

        })

        rvSettings.setOnClickListener {  }


    }


    private fun startPractiseSettingsDialog()
    {
        Log.d(TAG,"Current BookID = ${vocViewModel.getBookID()}")
        val dialog = DialogVocPractiseSettings(currentSetting!!)
        dialog.show(childFragmentManager,"Set the new Settings:")
        dialog.OnDialogClickListener(object:DialogVocPractiseSettings.OnDialogClickListener{
            override fun setOnDialogClickListener(newSetting: Setting) {
                vocViewModel.onAddNewSetting(newSetting)
                Log.d(TAG,"VocPracticeFragment - new Settings = $newSetting")
            }

        })
    }



}
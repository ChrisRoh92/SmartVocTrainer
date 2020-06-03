package com.example.voctrainer.moduls.voc.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Interpolator
import android.opengl.Visibility
import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.voctrainer.R
import com.example.voctrainer.moduls.voc.adapter.VocPractiseResultRecyclerViewAdapter
import com.example.voctrainer.moduls.voc.adapter.VocPractiseSettingsRecyclerViewAdapter
import com.example.voctrainer.moduls.voc.dialogs.DialogVocPractiseItemCount
import com.example.voctrainer.moduls.voc.dialogs.DialogVocPractiseSettingsMode
import com.example.voctrainer.moduls.voc.dialogs.DialogVocPractiseTime
import com.example.voctrainer.moduls.voc.viewmodel.VocViewModel
import com.example.voctrainer.moduls.voc.viewmodel.VocViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
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

    // View Elemente:
    private lateinit var fabStart:FloatingActionButton

    // RecyclerView:
    private lateinit var rvResults:RecyclerView
    private lateinit var managerResult:LinearLayoutManager
    private lateinit var adapterResult:VocPractiseResultRecyclerViewAdapter

    // RecyclerView for Practise Settings:
    private lateinit var rvSettings:RecyclerView
    private lateinit var managerSettings:LinearLayoutManager
    private lateinit var adapterSettings:VocPractiseSettingsRecyclerViewAdapter
    private lateinit var btnSave:Button
    private lateinit var btnAbortSave:Button

    // ViewModel:
    private lateinit var vocViewModel: VocViewModel
    private lateinit var vocViewModelFactory: VocViewModelFactory
    private var bookId:Long? = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bookId = arguments?.getLong("bookId",0)
        try {
            vocViewModelFactory = VocViewModelFactory(bookId!!,requireActivity().application)
            vocViewModel = ViewModelProvider(requireParentFragment(),vocViewModelFactory).get(VocViewModel::class.java)
            startObserver()
        } catch (e: Exception)
        {
            e.printStackTrace()

        }



    }

    private fun startObserver()
    {
        vocViewModel.tests.observe(viewLifecycleOwner, Observer { tests ->
            adapterResult.setNewContent(ArrayList(tests))
            Log.e("VocTrainer","$tests")
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_voc_practise, container, false)

        initViews()
        initRecyclerView()
        initSettingsRecyclerView()
        return rootView
    }

    private fun initViews()
    {
        // FloatingActionButton:
        fabStart = rootView.findViewById(R.id.fragment_voc_practise_fab)
        fabStart.setOnClickListener {
            //Aktuelle Settings hinzufügen...
            //findNavController().navigate(R.id.action_global_nested_practise)
        }
        btnSave = rootView.findViewById(R.id.fragment_voc_practise_btn_save)
        btnAbortSave = rootView.findViewById(R.id.fragment_voc_practise_btn_abort)

        fun setButtonVisibility()
        {
            // TODO(): Animation einbauen


            /*btnSave.visibility = View.GONE
            btnAbortSave.visibility = View.GONE*/



            btnSave.animate()
                .alpha(0.0f)
                .translationY(-10f)
                .setListener(object:Animator.AnimatorListener{
                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        btnSave.visibility = View.GONE
                        btnSave.clearAnimation()
                    }

                    override fun onAnimationCancel(animation: Animator?) {

                    }

                    override fun onAnimationStart(animation: Animator?) {

                    }

                })
                .setDuration(500)
                .start()

            btnAbortSave.animate()
                .alpha(0.0f)
                .translationY(-10f)
                .setListener(object:Animator.AnimatorListener{
                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        btnAbortSave.visibility = View.GONE
                        btnAbortSave.clearAnimation()
                    }

                    override fun onAnimationCancel(animation: Animator?) {

                    }

                    override fun onAnimationStart(animation: Animator?) {

                    }

                })
                .setDuration(500)
                .start()

        }

        btnSave.setOnClickListener {
            // TODO(): Speichern der Settings
            setButtonVisibility() }
        btnAbortSave.setOnClickListener { setButtonVisibility() }
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
                findNavController().navigate(R.id.action_global_nested_practise)
            }

        })

    }

    private fun initSettingsRecyclerView()
    {
        // TODO() - Darf erst aufgerufen werden, wenn die aktuelle Settings verfügbar ist --> Observer
        rvSettings = rootView.findViewById(R.id.fragment_voc_practise_rv_settings)
        managerSettings = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        adapterSettings = VocPractiseSettingsRecyclerViewAdapter()
        rvSettings.setHasFixedSize(true)
        rvSettings.layoutManager = managerSettings
        rvSettings.adapter = adapterSettings
        adapterSettings.setOnItemClickListener(object:VocPractiseSettingsRecyclerViewAdapter.OnItemClickListener{
            override fun setOnItemClickListener(position: Int)
            {
                startSettingsDialog(position)

            }

        })
    }

    // Methode ruft je nach Position ein Dialog auf:
    private fun startSettingsDialog(position:Int)
    {

        
        if(position == 0)
        {
            // Anzahl Vokabeln festlegen:
            // TODO(), hier kommt die Zahl aus der aktuellen Settings vom ViewModel rein....
            var dialog = DialogVocPractiseItemCount(-1)
            dialog.show(parentFragmentManager,"ItemCount")
            dialog.setOnItemCountClickListener(object:DialogVocPractiseItemCount.OnItemCountClickListener{
                override fun setOnItemCountClickListener(itemCount: Int) {

                    showButtonsToSave()
                }

            })

        }
        else if(position == 1)
        {
            showButtonsToSave()
        }
        else if (position == 2)
        {
            // Zeit festlegen:
            var dialog = DialogVocPractiseTime()
            dialog.show(parentFragmentManager,"Time")
            dialog.setOnTimeClickListener(object:DialogVocPractiseTime.OnTimeClickListener{
                override fun setOnTimeClickListener(hour: Int, minute: Int, second: Int) {
                    showButtonsToSave()
                }

            })
        }
        else if(position == 3)
        {
            // SettingsMode (Direkt bewerten oder am Ende) festlegen:
            var dialog = DialogVocPractiseSettingsMode()
            dialog.show(parentFragmentManager,"SettingsMode")
            dialog.setOnSettingsModeClickListener(object:DialogVocPractiseSettingsMode.OnSettingsModeClickListener{
                override fun setOnSettingsModeClickListener(settingsMode: Int) {
                    showButtonsToSave()
                }

            })

        }
        else
        {
            showButtonsToSave()
        }
    }

    private fun showButtonsToSave()
    {
        /*val snackbar = Snackbar
//                .make(rootView,"Neue Einstellungen speichern?",Snackbar.LENGTH_INDEFINITE)
//                .setAction("Speichern"){
//                    Toast.makeText(requireContext(),"Änderungen gespeichert",Toast.LENGTH_SHORT).show()
//                }
//
//            snackbar.show()
        val viewpager = requireParentFragment().view?.findViewById<ViewPager2>(R.id.fragment_vocs_viewpager2)
        viewpager.on*/
        // TODO(): Animation einbauen
        if(btnSave.visibility != View.VISIBLE)
        {
            btnSave.animate()
                .alpha(1.0f)
                .scaleY(1f)
                .setListener(object:Animator.AnimatorListener{
                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        btnSave.visibility = View.VISIBLE
                        btnSave.clearAnimation()
                    }

                    override fun onAnimationCancel(animation: Animator?) {

                    }

                    override fun onAnimationStart(animation: Animator?) {
                        btnSave.visibility = View.VISIBLE
                        btnSave.scaleY = 0f

                    }

                })
                .setStartDelay(200)
                .setDuration(500)
                .start()

            btnAbortSave.animate()
                .alpha(1.0f)
                .scaleY(1f)
                .setListener(object:Animator.AnimatorListener{
                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        btnAbortSave.visibility = View.VISIBLE
                        btnAbortSave.clearAnimation()
                    }

                    override fun onAnimationCancel(animation: Animator?) {

                    }

                    override fun onAnimationStart(animation: Animator?) {
                        btnAbortSave.visibility = View.VISIBLE
                        btnAbortSave.scaleY = 0f
                    }

                })
                .setStartDelay(200)
                .setDuration(500)
                .start()
        }


    }


}
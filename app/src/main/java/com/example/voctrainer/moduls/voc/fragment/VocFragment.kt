package com.example.voctrainer.moduls.voc.fragment


import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.voctrainer.R
import com.example.voctrainer.moduls.voc.adapter.VocStateAdapter
import com.example.voctrainer.moduls.voc.dialogs.DialogImportVocData
import com.example.voctrainer.moduls.voc.viewmodel.VocViewModel
import com.example.voctrainer.moduls.voc.viewmodel.VocViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class VocFragment : Fragment(), SearchView.OnQueryTextListener {

   // Allgemeine Variablen:
    private lateinit var rootView:View

    // Stuff für den ViewPager2:
    lateinit var viewPager2: ViewPager2
    private lateinit var adapter:VocStateAdapter
    private lateinit var tabLayout:TabLayout
    private val tabText:ArrayList<String> = arrayListOf("Home","Vokabeln","Üben","Statistik")
    private var currentPos = 0

    // Toolbar:
    private var toolbar: Toolbar? = null
    private var correctMenu = false

    // NotificationChannel:

    // ViewModel:
    private lateinit var vocViewModel:VocViewModel
    private lateinit var vocViewModelFactory: VocViewModelFactory
    private var bookId:Long? = 0L
    private var searchItem:MenuItem? = null
    private var searchView:SearchView? = null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_voc, container, false)


        initViewPager2()
        if(toolbar!=null)
        initToolBar()

        requireActivity().onBackPressedDispatcher.addCallback(this,object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_voc_main)
            }

        })






        return rootView
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        /*Log.e("VocFragment","bookId = $bookId")
        Log.e("VocFragment","application = ${activity!!.application}")
        vocViewModelFactory = VocViewModelFactory(bookId!!,activity!!.application)
        vocViewModel = ViewModelProvider(this,vocViewModelFactory).get(VocViewModel::class.java)*/
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bookId = arguments?.getLong("bookId",0L)
        vocViewModelFactory = VocViewModelFactory(bookId!!,requireActivity().application)
        vocViewModel = ViewModelProvider(this,vocViewModelFactory).get(VocViewModel::class.java)
        startObserver()


    }

    private fun startObserver()
    {
        vocViewModel.book.observe(viewLifecycleOwner, Observer{book ->
            if(toolbar==null)
            {
                initToolBar()
                toolbar!!.title = book.name
                toolbar!!.subtitle = "Erstellt am ${book.timeStamp}"
            }
            else
            {
                toolbar!!.title = book.name
                toolbar!!.subtitle = "Erstellt am ${book.timeStamp}"
            }
        })
    }



    // ViewPager2 initialisieren:
    private fun initViewPager2()
    {
        viewPager2 = rootView.findViewById(R.id.fragment_vocs_viewpager2)
        adapter = VocStateAdapter(this,bookId!!)
        viewPager2.adapter = adapter

        // TabLayout Stuff:
        tabLayout = rootView.findViewById(R.id.fragment_voc_tablayout)
        TabLayoutMediator(tabLayout,viewPager2){tab,position ->
            tab.text = tabText[position]

        }.attach()

        // Current Item:
        viewPager2.setPageTransformer { page, position ->
           if(viewPager2.currentItem != currentPos)
           {

               changeToolBarMenu(currentPos,viewPager2.currentItem)
               currentPos = viewPager2.currentItem
               //Toast.makeText(rootView.context,"Aktuelle Position: $currentPos",Toast.LENGTH_SHORT).show()

           }
        }




    }

    private fun initToolBar()
    {
        toolbar = rootView.findViewById(R.id.fragment_voc_toolbar)
        toolbar!!.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_voc_main)
        }






        toolbar!!.setOnMenuItemClickListener {
            if(it.itemId == R.id.menu_voc_import)
            {
                /*var bundle = bundleOf("source" to 1)
                findNavController().navigate(R.id.action_voc_setting,bundle)*/
                var dialog = DialogImportVocData()
                dialog.show(childFragmentManager,"")
                dialog.setOnDialogClickListener(object:DialogImportVocData.OnDialogClickListener{
                    override fun setOnDialogClickListener() {

                    }

                })

            }
            else if(it.itemId == R.id.menu_voc_data_search)
            {
                // Suchen von Stuff
            }
            else if (it.itemId == R.id.menu_voc_settings)
            {



            }
            true
        }


    }

    private fun changeToolBarMenu(oldPos:Int,newPos:Int)
    {
        if(newPos == 1)
        {
            // VocData Menu aufrufen...
            toolbar!!.menu.clear()
            toolbar!!.inflateMenu(R.menu.menu_voc_data)
            searchItem = toolbar!!.menu.findItem(R.id.menu_voc_data_search)
            searchView = searchItem!!.actionView as SearchView
            searchView!!.setOnQueryTextListener(this)
        }
        else if(newPos != 1 && oldPos == 1)
        {
            toolbar!!.menu.clear()
            toolbar!!.inflateMenu(R.menu.menu_voc_toolbar)




        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        vocViewModel.onFilterVocs(query!!)
        Log.d("VocTrainer","VocFragment - onQueryTextSubmit query = ${query!!}")
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(TextUtils.isEmpty(newText))
        {
            vocViewModel.onFilterVocs("")
        }
        else
        {
            Log.d("VocTrainer","VocFragment - onQueryTextChange query = ${newText!!}")
            vocViewModel.onFilterVocs(newText!!)
        }
        return true
    }


}






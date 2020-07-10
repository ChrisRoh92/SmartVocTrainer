package com.example.voctrainer.moduls.voc.fragment


import android.content.Intent
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
    private val tabText:ArrayList<String> = arrayListOf("Vokabeln","Üben","Statistik")
    private var currentPos = 0

    // Toolbar:
    private var toolbar: Toolbar? = null


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









        return rootView
    }





    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bookId = arguments?.getLong("bookId",0L)
        vocViewModelFactory = VocViewModelFactory(bookId!!,requireActivity().application)
        vocViewModel = ViewModelProvider(this,vocViewModelFactory).get(VocViewModel::class.java)
        vocViewModel.book.observe(viewLifecycleOwner, Observer {
            if(toolbar != null)
            {
                toolbar?.title = it.name
            }
            else
            {
                initToolBar()
                toolbar?.title = it.name
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






    }

    private fun initToolBar()
    {
        toolbar = rootView.findViewById(R.id.fragment_voc_toolbar)
        // Back button Handeln:
        toolbar!!.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_voc_main)
        }

        // Init SearchView:
        val searchItem = toolbar!!.menu.findItem(R.id.menu_voc_data_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnSearchClickListener {
            if(viewPager2.currentItem != 0)
            {
                viewPager2.setCurrentItem(0,true)
                Log.d("VocTrainer","ViewPager2, please scroll to Position 1") }
            }

        searchView.setOnQueryTextListener(this)


        // Handeln der verschiedenen Items:
        toolbar!!.setOnMenuItemClickListener {
            // Import Click:




            if(it.itemId == R.id.menu_voc_import)
            {
                // Start Import Work:
                var dialog = DialogImportVocData()
                dialog.show(childFragmentManager,"")
                dialog.setOnContentClickListener(object:DialogImportVocData.OnContentClickListener{
                    override fun setOnContentClickListener() {
                        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                        intent.type = "*/*"
                        intent.addCategory(Intent.CATEGORY_OPENABLE)
                        intent.flags =
                            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true)
                        val requestCode = 155
                        startActivityForResult(intent,requestCode)
                    }

                })
                dialog.setOnDialogClickListener(object:DialogImportVocData.OnDialogClickListener{
                    override fun setOnDialogClickListener() {

                    }

                })

            }

            // In die Einstellungen gehen
            else if (it.itemId == R.id.menu_voc_settings)
            {



            }




            true
        }


    }




    // Und hier aus import, das Ganze auch entgegen nehmen...
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 155)
        {
            var uriPath = data!!.data!!
            val bundle = Bundle()
            bundle.putString("uri",uriPath.toString())
            bundle.putLong("bookID",bookId!!)
            findNavController().navigate(R.id.action_voc_csvimport, bundle)

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
        /*vocViewModel.onFilterVocs(query!!)
        Log.d("VocTrainer","VocFragment - onQueryTextSubmit query = ${query!!}")*/
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        /*if(TextUtils.isEmpty(newText))
        {
            vocViewModel.onFilterVocs("")
        }
        else
        {
            Log.d("VocTrainer","VocFragment - onQueryTextChange query = ${newText!!}")
            vocViewModel.onFilterVocs(newText!!)
        }*/
        return true
    }


}






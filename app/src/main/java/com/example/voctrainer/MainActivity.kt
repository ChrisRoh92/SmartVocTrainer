package com.example.voctrainer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.voctrainer.moduls.standard.dialogs.DialogStandardAlert

class MainActivity : AppCompatActivity() {

    private var back_pressed:Long = 0
    private var practiseBack = false


    private lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        initNavController()

        // Wenn Intent empfangen wird, dann soll das ganze von hier aus zu dem Fragment csv_import
        // umgeleitet werden...
        if(intent?.action == Intent.ACTION_VIEW)
        {
            Log.d("VocTrainer","MainActivity - Intent wurde erkannt")

            if(true)
            {
                Log.d("VocTrainer","MainActivity - Intent-Type passt auch")
                var uriPath = intent.data!!.toString()
                Log.d("VocTrainer","MainActivity - uriPath = $uriPath")
                var bundle = bundleOf("uri" to uriPath)
                bundle.putLong("BookId",1)
                navController.navigate(R.id.CSVImportFragment, bundle)

            }
        }








    }


    override fun onBackPressed() {
        if(navController.currentDestination!!.id == R.id.mainFragment)
        {
            if(back_pressed+3000 > System.currentTimeMillis())
            {
                super.onBackPressed()
            }
            else
            {
                Toast.makeText(this,"Zum Beenden nochmal drücken",Toast.LENGTH_LONG).show()
            }
            back_pressed = System.currentTimeMillis()
        }
        else
        {
            super.onBackPressed()
        }
        /*else if(navController.currentDestination!!.id == R.id.practiseFragment && !practiseBack)
        {
            var dialog = DialogStandardAlert("Möchten Sie den Versuch wirklich abbrechen?","Dieser Vorgang kann nicht rückgängig gemacht werden")
            dialog.show(supportFragmentManager,"")
            dialog.setOnDialogClickListener(object: DialogStandardAlert.OnDialogClickListener{
                override fun setOnDialogClickListener() {
                    practiseBack = true
                    onBackPressed()
                }

            })
        }
        else
        {
            practiseBack = false
            super.onBackPressed()

        }*/

    }

    private fun initNavController()
    {
        val host:NavHostFragment = supportFragmentManager.findFragmentById(R.id.main_navhost) as NavHostFragment
        navController = host.navController

    }






}
  
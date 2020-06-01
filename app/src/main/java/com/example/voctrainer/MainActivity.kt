package com.example.voctrainer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {


    private lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        initNavController()

        // Wenn Intent empfangen wird, dann soll das ganze von hier aus zu dem Fragment csv_import
        // umgeleitet werden...
        if(intent?.action == Intent.ACTION_VIEW)
        {
            if(intent?.type == "text/comma-separated-values")
            {
                var uriPath = intent.data!!.path

                val bundle = bundleOf("uri" to uriPath)
                navController.navigate(R.id.action_global_csv_import, bundle)

            }
        }








    }



    private fun initNavController()
    {
        val host:NavHostFragment = supportFragmentManager.findFragmentById(R.id.main_navhost) as NavHostFragment
        navController = host.navController

    }






}

package com.example.voctrainer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.FileProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.voctrainer.backend.database.entities.Book
import com.example.voctrainer.backend.database.entities.BookWithSettings
import com.example.voctrainer.backend.database.entities.Settings
import java.io.File

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        initNavController()





    }



    private fun initNavController()
    {
        val host:NavHostFragment = supportFragmentManager.findFragmentById(R.id.main_navhost) as NavHostFragment
        val navController = host.navController

    }






}

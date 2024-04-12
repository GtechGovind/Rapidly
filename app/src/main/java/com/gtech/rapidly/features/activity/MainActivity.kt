package com.gtech.rapidly.features.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cafe.adriel.voyager.navigator.Navigator
import com.gtech.rapidly.features.common.ui.utils.WithTheme

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var instance: MainActivity
            private set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        enableEdgeToEdge()
        setContent {
            WithTheme {
                Navigator(MainScreen())
            }
        }
    }

}
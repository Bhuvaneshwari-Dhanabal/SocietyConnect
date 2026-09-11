package com.example.societyconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.societyconnect.navigation.AppNavigation
import com.example.societyconnect.ui.theme.SocietyConnectTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SocietyConnectTheme {
                AppNavigation()
            }
        }
    }
}
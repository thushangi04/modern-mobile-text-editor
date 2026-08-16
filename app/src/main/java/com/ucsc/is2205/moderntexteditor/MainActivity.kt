package com.ucsc.is2205.moderntexteditor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ucsc.is2205.moderntexteditor.navigation.AppNavigation
import com.ucsc.is2205.moderntexteditor.ui.theme.ModernTextEditorTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            ModernTextEditorTheme {
                AppNavigation()
            }
        }
    }
}

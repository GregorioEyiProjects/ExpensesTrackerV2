package com.example.expensestrackercomposablev2.appComponents

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SetBarColor(color: Color) {
    val setSystemUIController = rememberSystemUiController()
    SideEffect {
        setSystemUIController.setSystemBarsColor(color)
    }
}

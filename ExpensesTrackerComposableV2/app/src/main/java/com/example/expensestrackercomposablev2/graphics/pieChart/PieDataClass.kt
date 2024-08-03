package com.example.expensestrackercomposablev2.graphics.pieChart

import androidx.compose.ui.graphics.Color

data class PieDataClass(
    val color: Color,
    val value:Int,
    val description:String,
    val isTapped:Boolean = false
)

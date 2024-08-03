package com.example.expensestrackercomposablev2.models

import androidx.compose.ui.graphics.Color

data class ItemPurchaseV2(
    val color: Color,
    val value:Int,
    val description:String,
    val isTapped:Boolean = false
)

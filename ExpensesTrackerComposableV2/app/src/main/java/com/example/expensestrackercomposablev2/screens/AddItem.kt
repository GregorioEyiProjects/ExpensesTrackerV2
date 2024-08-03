package com.example.expensestrackercomposablev2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensestrackercomposablev2.appComponents.BottomNavigationBar
import com.example.expensestrackercomposablev2.appComponents.ButtonBack
import com.example.expensestrackercomposablev2.appComponents.TextComponent
import com.example.expensestrackercomposablev2.appComponents.ShowTextFieldPriceAndCategory
import com.example.expensestrackercomposablev2.viewModels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItem(
    destinationPage: NavController,
    viewModel: MainViewModel
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .wrapContentWidth(align = Alignment.CenterHorizontally),
        topBar = { // To display this component at the top of the screen.
            CenterAlignedTopAppBar(
                title = {
                    TextComponent(
                        tittleText = "Add an item",
                        colorText = Color.White,
                        fontWeightText = FontWeight.Bold
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .height(45.dp),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                navigationIcon = {
                    ButtonBack(destinationPage)
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(destinationPage = destinationPage)
        }


    ) {
        AddItemContainer(it, destinationPage, viewModel)
    }
}

@Composable
fun AddItemContainer(
    it: PaddingValues,
    destinationPage: NavController,
    viewModel: MainViewModel
) {
    Column(
        modifier = Modifier
            .padding(it)
            .padding(top = 30.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Enter the amount you just spent \nAnd the category",
            color = Color.White,
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(top = 40.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )

        /*Quantity text field to enter the amount spent*/
        ShowTextFieldPriceAndCategory(
            textValue = "Enter the amount spent",
            destinationPage = destinationPage,
            viewModel = viewModel
        )

    }

}

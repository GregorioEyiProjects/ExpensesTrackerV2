package com.example.expensestrackercomposablev2.appComponents


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expensestrackercomposablev2.location.LocationManagerV2
import com.example.expensestrackercomposablev2.ui.theme.white
import com.example.expensestrackercomposablev2.viewModels.MainViewModel

@Composable
fun ButtonBack(destinationPage: NavController) {
    IconButton(onClick = {
        destinationPage.popBackStack()
    }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "back",
            tint = white
        )
    }
}

//I was using in Home screen to navigate throughout an icon to the Pie chart. Now i am using a bottom bar
@Composable
fun ShowPie(destinationPage: NavController) {
    IconButton(onClick = { destinationPage.navigate("pieChart") }) {
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Pie")
    }
}

@Composable
fun ShowTextFieldPriceAndCategory(
    textValue: String,
    destinationPage: NavController,
    viewModel: MainViewModel
) {

    var quantityValue by remember { mutableStateOf("") }
    var errorState by remember { mutableStateOf(false) }

    val localFocusManager = LocalFocusManager.current // clear the focus

    //Price OUTLINE TEXT FIELD
    OutlinedTextField(
        value = quantityValue,
        onValueChange = { quantityValue = it },
        label = { Text(text = "Price") },
        placeholder = {
            Text(text = textValue)
        },
        // leadingIcon = { Icons.Default.Add },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus(true)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .padding(bottom = 15.dp, top = 15.dp)
    )

    //Category OUTLINE TEXT FIELD
    val categoryValue = textFieldCategory()

    // Button to Add the ITEM to the DB
    if (quantityValue.isEmpty() || categoryValue.isEmpty()) {
        errorState = true // not need the error state
    } else {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 25.dp),
            contentAlignment = Alignment.BottomCenter
        ){

            LocationManagerV2(
                quantity = quantityValue,
                category = categoryValue,
                viewModel = viewModel,
                destinationPage = destinationPage
            )

        }


    }


}


@Composable
fun textFieldCategory(): String {

    var categoryValue by remember { mutableStateOf("") }
    var expandedValue by remember { mutableStateOf(false) }

    val options = listOf(
        "Food & Drinks",
        "Auto & Transport",
        "Healthcare",
        "Withdrawal",
        "Shopping",
        "Entertainment",
        "Education",
        "Barbershop",
        "Hotel & Travel",
        "Other"
    )

    val icon = if (expandedValue) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown

    OutlinedTextField(
        value = categoryValue.trim(),
        onValueChange = { categoryValue = it },
        label = { Text("Category") },
        trailingIcon = {
            Icon(
                icon, "contentDescription",
                modifier = Modifier.clickable { expandedValue = !expandedValue }
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        maxLines = 1,
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .padding(bottom = 15.dp)
            .fillMaxWidth()
    )

    DropdownMenu(
        expanded = expandedValue,
        onDismissRequest = { expandedValue = false }
    ) {
        options.forEach { item ->
            DropdownMenuItem(
                onClick = {
                    categoryValue = item
                    expandedValue = false
                },
                text = { Text(text = item) }
            )
        }
    }

    return categoryValue
}









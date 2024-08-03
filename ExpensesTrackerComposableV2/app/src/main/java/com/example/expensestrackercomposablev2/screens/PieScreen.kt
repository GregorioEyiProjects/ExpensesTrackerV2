package com.example.expensestrackercomposablev2.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.expensestrackercomposablev2.appComponents.BottomNavigationBar
import com.example.expensestrackercomposablev2.appComponents.ButtonBack
import com.example.expensestrackercomposablev2.appComponents.ShowDialog
import com.example.expensestrackercomposablev2.appComponents.TextComponent
import com.example.expensestrackercomposablev2.graphics.pieChart.PieChartComponentV2
import com.example.expensestrackercomposablev2.models.ItemPurchase
import com.example.expensestrackercomposablev2.models.ItemPurchaseV2
import com.example.expensestrackercomposablev2.ui.theme.Pink40
import com.example.expensestrackercomposablev2.ui.theme.Purple40
import com.example.expensestrackercomposablev2.ui.theme.PurpleGrey40
import com.example.expensestrackercomposablev2.ui.theme.blue
import com.example.expensestrackercomposablev2.ui.theme.blueGray
import com.example.expensestrackercomposablev2.ui.theme.brightBlue
import com.example.expensestrackercomposablev2.ui.theme.darkBlue
import com.example.expensestrackercomposablev2.ui.theme.darkGray
import com.example.expensestrackercomposablev2.ui.theme.green
import com.example.expensestrackercomposablev2.ui.theme.nightDark
import com.example.expensestrackercomposablev2.ui.theme.orange
import com.example.expensestrackercomposablev2.ui.theme.purple
import com.example.expensestrackercomposablev2.ui.theme.redOrange
import com.example.expensestrackercomposablev2.viewModels.MainViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PieChartScreen(
    destinationPage: NavController,
    listOfItems: List<ItemPurchase>,
    viewModel: MainViewModel
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        topBar = { // To display this component at the top of the screen.
            CenterAlignedTopAppBar(
                title = {
                    TextComponent(
                        tittleText = "Money was spent on",
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
        PieChartContent(it, viewModel, listOfItems)
    }


}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PieChartContent(
    it: PaddingValues,
    viewModel: MainViewModel,
    listOfItems: List<ItemPurchase>
) {


    Column(
        modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clip(RoundedCornerShape(20.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            /*(START) --------- of the PieChartComponentV2 */
            // List of colors
            val colors = listOf(
                darkGray,
                darkBlue, // Add your desired theme colors here
                orange,
                blueGray,
                nightDark,
                redOrange,
                green,
                blue,
                brightBlue
            )
            val items: MutableList<ItemPurchaseV2> = mutableListOf()

            val quantityMap = HashMap<String, Double>()

            listOfItems.forEach { item ->

                val thisItemName = item.itemName.trim()
                val money = item.quantity

                /*This code iterates through listOfItems, checks if the item name exists in the quantityMap.
                If it does, it adds the current item's quantity to the existing value.
                If not, it adds the item name and its quantity as a new entry*/
                if (quantityMap.containsKey(thisItemName)) {
                    quantityMap[thisItemName] =
                        quantityMap[thisItemName]!! + money // We're accessing the value associated with the key itemName_ using quantityMap[itemName_]
                } else {
                    quantityMap[thisItemName] = money
                }

            }

            quantityMap.forEach { (itemName, quantity) ->
                // val randomIndex = (colors.indices.random() % colors.size) //to get a random index of the colors i have on the list

                Log.d("ITEM ", " $itemName")

                val colorToUse = when (itemName) {
                    "Food & Drinks" -> orange
                    "Auto & Transport" -> brightBlue
                    "Healthcare" -> green
                    "Withdrawal" -> darkBlue
                    "Shopping" -> blue
                    "Entertainment" -> purple
                    "Education" -> Purple40
                    "Barbershop" -> blueGray
                    "Hotel & Travel" -> nightDark
                    "Other" -> PurpleGrey40
                    else -> Pink40
                }

                Log.d("Color for $itemName: ", colorToUse.toString())

                items.add(
                    ItemPurchaseV2(
                        color = colorToUse ?: colors.random(),
                        value = quantity.toInt(),
                        description = itemName
                    )
                )

            }

            PieChartComponentV2(
                modifier = Modifier.size(380.dp),
                inputData = items,
                centerText = "Tap here or around"
            )
            /*(END) --------- of the PieChartComponentV2 */

        }

        ExpensesHistory(

            listOfItems,
            viewModel
        )

    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ExpensesHistory(

    itemPurchase: List<ItemPurchase>,
    viewModel: MainViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .padding(top = 15.dp)
            .animateContentSize()
            .padding(horizontal = 15.dp)

    ) {

        //var iconWasClicked by remember { mutableStateOf(false) }
        var iconPosition by remember { mutableStateOf(Icons.Rounded.KeyboardArrowDown) }
        var expandedValue by remember { mutableStateOf(false) }
        val listOfDefaultText = listOf("All expenses", "Today", "This week", "This month", "Custom")
        var selectedText by remember { mutableStateOf(listOfDefaultText[0]) }

        //Expenses History text
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Text(
                text = "History Expenses",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.Start)
            )

            // Row to display the icon and the text
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {

                //Text before the icon
                Text(
                    text = selectedText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.clickable {
                        expandedValue = !expandedValue
                        iconPosition = if (expandedValue) {
                            Icons.Rounded.KeyboardArrowUp
                        } else {
                            Icons.Rounded.KeyboardArrowDown
                        }
                    }
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Expenses summary Icon
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary)
                        .clickable {
                            Log.d("Clicking?", "YES")
                            expandedValue = !expandedValue
                            iconPosition = if (expandedValue) {
                                Icons.Rounded.KeyboardArrowUp
                            } else {
                                Icons.Rounded.KeyboardArrowDown
                            }
                        }
                ) {

                    Icon(
                        imageVector = iconPosition,
                        contentDescription = "Expenses summary",
                        modifier = Modifier.size(25.dp),
                        tint = Color.White
                    )

                } // END of the ICON to display the Dropdown menu

            } // END of Row to display the icon and the text

        } //END of Expenses History text

        DropdownMenu(
            expanded = expandedValue,
            onDismissRequest = {
                expandedValue = false
                iconPosition = Icons.Rounded.KeyboardArrowDown
            }
        ) {

            listOfDefaultText.forEach { textValue ->
                DropdownMenuItem(
                    text = {
                        Text(text = textValue)
                    },
                    onClick = {
                        selectedText = textValue
                        expandedValue = false
                        iconPosition = Icons.Rounded.KeyboardArrowDown
                        if (selectedText == "Custom"){
                            viewModel.filterExpensesByTimePeriod(selectedText)
                        }else{
                            viewModel.filterExpensesByTimePeriod(selectedText)
                        }

                    }
                )
            }

            /*LaunchedEffect(selectedText) {
                viewModel.filterExpensesByTimePeriod(selectedText)
            }*/

        }


        Spacer(modifier = Modifier.padding(top = 5.dp))
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onSecondaryContainer)
        )

        //Expenses History details
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {

            val boxWithConstraintsScope = this
            val with = boxWithConstraintsScope.maxWidth / 2

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
            ) {

                Spacer(modifier = Modifier.height(5.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Category",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        modifier = Modifier.width(with),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = "Price",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        modifier = Modifier.width(with),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.End
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))


                val allItemsComing by viewModel.allItems.collectAsState()
                val todayItems by viewModel.todayItemsComing.collectAsState()
                val thisWeekItems by viewModel.thisWeekItemsComing.collectAsState()
                val thisMonthItems by viewModel.thisMonthItemsComing.collectAsState()
                val customItems by viewModel.customItemsComing.collectAsState()

              /*  Log.d("TodayItemsComing", "Size: ${todayItems.size}")
                todayItems.forEach { item ->
                    Log.d("TodayItemsComing", "Name: ${item.itemName}")
                    Log.d("TodayItemsComing", "Price ${item.quantity}")
                    Log.d("TodayItemsComing", "Date ${item.datePurchase}")
                    Log.d("TodayItemsComing", "Location ${item.location}")
                }*/
               /* Log.d("ThisWeekItemsComing", "Size: ${thisWeekItems.size}")
                thisWeekItems.forEach { item ->
                    Log.d("ThisWeekItemsComing", "Name: ${item.itemName}")
                    Log.d("ThisWeekItemsComing", "Price ${item.quantity}")
                    Log.d("ThisWeekItemsComing", "Date ${item.datePurchase}")
                    Log.d("ThisWeekItemsComing", "Location ${item.location}")
                }
                Log.d("ThisMonthItemsComing", "Size: ${thisMonthItems.size}")
                thisMonthItems.forEach { item ->
                    Log.d("ThisMonthItemsComing", "Name: ${item.itemName}")
                    Log.d("ThisMonthItemsComing", "Price ${item.quantity}")
                    Log.d("ThisMonthItemsComing", "Date ${item.datePurchase}")
                    Log.d("ThisMonthItemsComing", "Location ${item.location}")
                }*/

                Log.d("CustomItemsComing", "Size: ${customItems.size}")
                /**/customItems.forEach { item ->
                    Log.d("CustomItemsComing", "Name: ${item.itemName}")
                    Log.d("CustomItemsComing", "Price ${item.quantity}")
                    Log.d("CustomItemsComing", "Date ${item.datePurchase}")
                    Log.d("CustomItemsComing", "Location ${item.location}")
                }

                val context = LocalContext.current
                val itemsToDisplay  = when (selectedText) {
                    "Today" -> todayItems
                    "This week" -> thisWeekItems
                    "This month" -> thisMonthItems
                    "Custom" -> allItemsComing
                    else -> allItemsComing
                }

                LazyColumn {
                    items(itemsToDisplay) { item ->
                        Log.d("WhatItemIsThisOne", item.itemName)
                        ExpensesItemHistory(
                            with,
                            item,
                            viewModel
                        )
                    }
                } //END of the Lazy colum
            }
        } //END of the BoxWithConstrains
    }
} // END of the "ExpensesHistory Component"

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ExpensesItemHistory(
    withComing: Dp,
    item: ItemPurchase,
    viewModel: MainViewModel
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Rounded.MonetizationOn,
            contentDescription = "Price",
            modifier = Modifier.size(18.dp),
            tint = Color.White
        )

        Text(
            text = item.itemName,
            modifier = Modifier
                .width(withComing)
                .padding(start = 10.dp)
                .clickable {
                    Log.d("Item?", "is ${item.location}") // correct location
                    viewModel.getItemDetails(item)
                },
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
        )

        Text(
            text = "${item.quantity} THB",
            modifier = Modifier
                .width(withComing)
                .padding(start = 10.dp),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End
        )

    } //END of Each ROW

    // To open a dialog with the items
    if (viewModel.itemDetails != null) {
        val context = LocalContext.current
        Dialog(onDismissRequest = viewModel::hideItemsDetails) {
            ShowDialog(context, viewModel)
        }
    }


}

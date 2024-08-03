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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensestrackercomposablev2.appComponents.BottomNavigationBar
import com.example.expensestrackercomposablev2.appComponents.SetBarColor
import com.example.expensestrackercomposablev2.appComponents.ShowPie
import com.example.expensestrackercomposablev2.appComponents.TextComponent
import com.example.expensestrackercomposablev2.models.ItemPurchase
import com.example.expensestrackercomposablev2.ui.theme.gray
import com.example.expensestrackercomposablev2.ui.theme.nightDark
import com.example.expensestrackercomposablev2.viewModels.MainViewModel


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    destinationPage: NavController, itemsList: List<ItemPurchase>, viewModel: MainViewModel
) {
    SetBarColor(color = MaterialTheme.colorScheme.background)

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .padding(15.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TextComponent(
                        tittleText = "HOME",
                        colorText = Color.White,
                        fontWeightText = FontWeight.Bold
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .height(45.dp),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    destinationPage.navigate("addItem")
                },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "AddItem")
            }
        },
        bottomBar = {
            BottomNavigationBar(destinationPage)
        }
    ) {
        HomeContent(it, itemsList, viewModel)
    }

}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomeContent(
    it: PaddingValues,
    itemsList: List<ItemPurchase>,
    viewModel: MainViewModel
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(gray)
    ) {
        Column() {

            var totalExpenses: Double = 0.0
            itemsList.forEach { item ->
                totalExpenses = (totalExpenses + item.quantity)
            }

            FirstContainer(it, totalExpenses.toString())

            //ShowItems(itemsList, viewModel)
            DisplayItemsOnScreen(
                paddingValues = it,
                itemsPurchase = itemsList,
                viewModel
            )

        }
    }


}

/*Container which contains the amount spent*/
@Composable
fun FirstContainer(
    it: PaddingValues, quantityValue: String = "1"
) {/*First container*/
    Column(
        modifier = Modifier
            .padding(it)
            .fillMaxWidth()
            .padding(top = 15.dp, bottom = 10.dp)
            .height(150.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.onSurfaceVariant)


    ) {


        TextComponent(
            tittleText = "Total expenses",
            fontSizeText = 20.sp,
            fontWeightText = FontWeight.Bold
        )

        /*To display the Total expenses*/
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            /*Total amount expenses*/
            TextComponent(
                tittleText = quantityValue,
                fontSizeText = 30.sp,
                fontWeightText = FontWeight.Bold
            )
            TextComponent(
                tittleText = "THB",
                fontSizeText = 30.sp,
                fontWeightText = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.heightIn(8.dp))

    }
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun DisplayItemsOnScreen(
    paddingValues: PaddingValues,
    itemsPurchase: List<ItemPurchase>,
    viewModel: MainViewModel
) {

    var isVisible by remember { mutableStateOf(false) }
    var iconState by remember { mutableStateOf(Icons.Rounded.KeyboardArrowDown) }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .animateContentSize()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
                    .padding(start = 25.dp, end = 10.dp)
                    .animateContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .clickable {
                        isVisible = !isVisible
                        iconState = if (isVisible) {
                            Icons.Rounded.KeyboardArrowUp
                        } else {
                            Icons.Rounded.KeyboardArrowDown
                        }
                    }) {

                    Icon(
                        imageVector = iconState,
                        contentDescription = "Expenses summary",
                        modifier = Modifier.size(25.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Expenses summary",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.clickable {
                        isVisible = !isVisible
                        iconState = if(isVisible){
                            Icons.Rounded.KeyboardArrowUp
                        }else{
                            Icons.Rounded.KeyboardArrowDown
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.padding(top = 10.dp))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            )
            //Logic to open and close the button

            if (isVisible) {

                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 15.dp)
                        .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                ) {

                    val boxWithConstraintsScope = this
                    val with = boxWithConstraintsScope.maxWidth / 2

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp)
                    ) {

                        Spacer(modifier = Modifier.height(15.dp))

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

                        Spacer(modifier = Modifier.height(15.dp))

                        val newItemPurchase = remember { mutableListOf<ItemPurchase>() }

                        //LaunchedEffect(itemsPurchase) {}
                        for (currentItem in itemsPurchase) {

                            val existingItem = newItemPurchase.find { item ->
                                item.itemName.trim() == currentItem.itemName.trim()
                            }

                            if (existingItem != null) {
                                existingItem.quantity += currentItem.quantity
                            } else {
                                newItemPurchase.add(ItemPurchase().apply {
                                    itemName = currentItem.itemName
                                    quantity = currentItem.quantity
                                    datePurchase = currentItem.datePurchase
                                    location = currentItem.location
                                })
                            }
                        }


                        LazyColumn {

                            items(newItemPurchase) { item ->

                                Log.d("NotItemHere", item.itemName)

                                ExpensesItem(
                                    with,
                                    item,
                                    viewModel
                                )
                            }
                        }

                    }

                }

            }

        }

    }

}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ExpensesItem(
    withComing: Dp,
    item: ItemPurchase,
    viewModel: MainViewModel
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(nightDark)
                .padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.MonetizationOn,
                contentDescription = "Price",
                modifier = Modifier.size(18.dp),
                tint = Color.White
            ) //  Change later
        }

        Text(
            text = item.itemName,
            modifier = Modifier
                .width(withComing)
                .padding(start = 10.dp)
                .clickable {
                    Log.d("Item?", "is ${item.location}") // correct location
                    viewModel.getItemDetails(item)
                    // OpenDialog(viewModel, location)
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

    }

}




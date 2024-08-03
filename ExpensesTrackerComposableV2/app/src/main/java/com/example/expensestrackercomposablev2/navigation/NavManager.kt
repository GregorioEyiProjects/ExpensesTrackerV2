package com.example.expensestrackercomposablev2.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensestrackercomposablev2.screens.AddItem
import com.example.expensestrackercomposablev2.screens.Home
import com.example.expensestrackercomposablev2.screens.Login
import com.example.expensestrackercomposablev2.screens.PieChartScreen
import com.example.expensestrackercomposablev2.screens.Register
import com.example.expensestrackercomposablev2.viewModels.MainViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NavManager(viewModel: MainViewModel) {

    val navController = rememberNavController()
    val itemsPurchase by viewModel.allItems.collectAsState()
    /*val todayItems by viewModel.thisWeekItemsComing!!.collectAsState()
    val thisWeekItems by viewModel.thisWeekItemsComing!!.collectAsState()
    val thisMonthItems by viewModel.thisWeekItemsComing!!.collectAsState()
    val customItems by viewModel.thisWeekItemsComing!!.collectAsState()*/

    NavHost(navController = navController, startDestination = "Login"){

        composable(route = "Login"){
            Login(
                destinationPage = navController
            )
        }

        composable(route = "Register"){
            Register(
                destinationPage = navController
            )
        }

        composable(route = "home"){
            Home(
                destinationPage = navController,
                itemsList = itemsPurchase,
                viewModel = viewModel
            )
        }

        composable(route = "addItem"){
            AddItem(
                destinationPage = navController,
                viewModel = viewModel
            )
        }

        composable(route = "pieChart"){
            Log.d("NavManager_PieChart", "Composable function called")
            PieChartScreen(
                destinationPage = navController,
                listOfItems = itemsPurchase,
                viewModel = viewModel
            )
        }

    }

}
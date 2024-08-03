package com.example.expensestrackercomposablev2.appComponents

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Details
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.expensestrackercomposablev2.models.BottomNavigationClass

val bottomNavItemList = listOf(
    BottomNavigationClass(
        title = "Home",
        iconNav = Icons.Rounded.Home,
        route = "home"
    ),
    BottomNavigationClass(
        title = "More",
        iconNav = Icons.Rounded.Details,
        route = "pieChart"
    ),
    BottomNavigationClass(
        title = "Settings",
        iconNav = Icons.Rounded.Settings,
        route = "settings"
    )
)


@Composable
fun BottomNavigationBar(
    destinationPage: NavController
) {
    //var selectedIcon by remember { mutableStateOf(0) }

    val navBackStackEntry by destinationPage.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    NavigationBar {
        Row(
            modifier = Modifier
                .height(75.dp)
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            bottomNavItemList.forEachIndexed { index, item ->

                val selectedIcon: Boolean = item.route == currentRoute
                NavigationBarItem(
                    selected = selectedIcon,
                    onClick = {
                        when (index) {
                            0 -> destinationPage.navigate(item.route)
                            1 -> destinationPage.navigate(item.route)
                            2 -> destinationPage.navigate("home")//item.route
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.iconNav,
                            contentDescription = item.title
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
            }
        }
    }

}
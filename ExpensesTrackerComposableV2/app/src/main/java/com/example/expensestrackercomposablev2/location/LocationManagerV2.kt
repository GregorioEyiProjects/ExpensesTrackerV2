package com.example.expensestrackercomposablev2.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.expensestrackercomposablev2.viewModels.MainViewModel
import com.google.android.gms.location.LocationServices
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


@SuppressLint("MissingPermission")
@Composable
fun LocationManagerV2(
    quantity: String,
    category: String,
    viewModel: MainViewModel,
    destinationPage: NavController
) {

    val context = LocalContext.current // 'context' acts like 'this' in a composable function

    var locationString: String = ""

    val fusedLocationClient =
        remember { LocationServices.getFusedLocationProviderClient(context) } // 'fusedLocationClient' to get the current location of the user

    val locationPermissionLauncher =
        rememberLauncherForActivityResult( // to check if the we have the permission the access the user's location
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let {

                            val latitude = location.latitude
                            val longitude = location.longitude
                            locationString = "Lat: $latitude, Long: $longitude"
                            val dateValue = getCurrentDate()

                            /**/
                            Log.d("TextField", " Quantity is ${quantity.toDouble()}")
                            Log.d("TextField", " Category is $category")
                            Log.d("TextField", " CurrentDate is $dateValue")
                            Log.d("TextField", " Location is $locationString")

                            /**/
                            viewModel.addItemToRealmDB(
                                itemQuantity = quantity.toDouble(),
                                itemName_ = category,
                                itemDate_ = dateValue,
                                itemLocation_ = locationString
                            )
                            destinationPage.navigate("home")

                        }
                    }
                }
            }
        )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Button(
            onClick = {

                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {

                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            val latitude = location.latitude
                            val longitude = location.longitude
                            locationString = "Lat: $latitude, Long: $longitude"
                            val dateValue = getCurrentDate()

                            Log.d("TextField", " Quantity is ${quantity.toDouble()}")
                            Log.d("TextField", " Category is $category")
                            Log.d("TextField", " CurrentDate is $dateValue")
                            Log.d("TextField", " Location is $locationString")

                            /**/viewModel.addItemToRealmDB(
                                itemQuantity = quantity.toDouble(),
                                itemName_ = category,
                                itemDate_ = dateValue,
                                itemLocation_ = locationString
                            )
                            destinationPage.navigate("home")
                        }
                    }

                } else {
                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = Color.White
            ),
            modifier = Modifier.width(150.dp)

        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Add item",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }

    }

}

fun getCurrentDate(): String {
    val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a", Locale.ENGLISH)
    return LocalDateTime.now().format(dateFormat)
}




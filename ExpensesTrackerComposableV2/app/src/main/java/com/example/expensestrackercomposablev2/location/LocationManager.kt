package com.example.expensestrackercomposablev2.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


//Local variables
const val REQUEST_LOCATION_CODE = 100 // Any unique code for permission request

@Composable
fun LocationManager(onLocationCaptured: (latitude: Double, longitude: Double) -> Unit) {

    //To access the current context within the composable.
    val context = LocalContext.current


    // State variable to store permission status
    var hasLocationPermission by remember { mutableStateOf(false) }

    // Request permission on launch (LaunchedEffect -> fetches the permission status on launch and requests permission if needed using 'requestLocationPermission')
    // The type with only one value: the 'Unit' object. This type corresponds to the 'void' type in Java.
    LaunchedEffect(Unit) {
        hasLocationPermission = checkLocationPermission(context)
        if (!hasLocationPermission) {
            requestLocationPermission(context)
        }
    }
    Button(onClick = {
        if (hasLocationPermission) {
            captureUserLocation(context, onLocationCaptured)
        } else {
            // Handle case where permission is denied
            Toast.makeText(context, "Allow the permission please", Toast.LENGTH_SHORT).show()
        }
    }) {
        Text("Capture Location")
    }


}


fun checkLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

fun requestLocationPermission(context: Context) {
    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    ActivityCompat.requestPermissions(context as Activity, permissions, REQUEST_LOCATION_CODE)
}

fun captureUserLocation(
    context: Context,
    onLocationCaptured: (latitude: Double, longitude: Double) -> Unit
) {
    val locationManager_ = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        val location = locationManager_.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if(location!=null){
            onLocationCaptured(location.latitude, location.longitude)
        }else {
            // Handle case where location is not available
            Toast.makeText(context, "Location is not available", Toast.LENGTH_SHORT).show()
        }
    }
}
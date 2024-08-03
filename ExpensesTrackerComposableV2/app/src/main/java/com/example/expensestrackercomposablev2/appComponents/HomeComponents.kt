package com.example.expensestrackercomposablev2.appComponents

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensestrackercomposablev2.models.LocationData
import com.example.expensestrackercomposablev2.viewModels.MainViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.Locale



/* Editable Text component for the Home screen*/

@Composable
fun TextComponent(
    tittleText: String,
    alignmentText: TextAlign = TextAlign.Start,
    colorText: Color = Color.Black,
    fontSizeText: TextUnit = 16.sp,
    fontWeightText: FontWeight = FontWeight.Normal,
    fontStyleText: FontStyle = FontStyle.Normal,
    maxLinesText: Int? = null
) {
    Text(
        modifier = Modifier
            .wrapContentWidth(align = Alignment.Start)
            .wrapContentHeight(align = Alignment.Top)
            .alpha(0.8f)
            .padding(10.dp),
        text = tittleText,
        textAlign = alignmentText,
        color = colorText,
        fontSize = fontSizeText,
        fontWeight = fontWeightText,
        fontStyle = fontStyleText,
        maxLines = maxLinesText ?: Int.MAX_VALUE
    )

}

@Composable
fun ShowLocation(locationData: LocationData?) {
    if (locationData != null) {
        Text("Location: Lat: ${locationData.latitude}, Lng: ${locationData.longitude}")
    } else {
        Text("Location data unavailable.")
    }
}


@Composable
fun MapComponent(location: String) {

    val parts = location.split(", ")
    Log.d("Location", "is $location")
    val latitude = parts[0].substringAfter("Lat: ").toDouble()
    val longitude = parts[1].substringAfter("Long: ").toDouble()

    Log.d("Lat", "$latitude")
    Log.d("Long", "$longitude")

    val place = LatLng(latitude, longitude)
    val myCameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(place, 12f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .height(200.dp)
    ) {
        GoogleMap(
            cameraPositionState = myCameraPosition
        ) {
            Marker(
                state = MarkerState(position = place),
                title = "Thailand",
                snippet = "Maker in"
            )
        }
    }


}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun getAddressFromLatLng(
    latLng: String,
    context: Context
): String {

    var knownName = ""
    val parts = latLng.split(", ")
    Log.d("Location_", "is $latLng")
    val latitude = parts[0].substringAfter("Lat: ").toDouble()
    val longitude = parts[1].substringAfter("Long: ").toDouble()

    val geocoder = Geocoder(context, Locale.getDefault())

    try {

        val addresses = geocoder.getFromLocation(latitude, longitude, 1)

        if (addresses != null) {

            knownName = addresses[0].getAddressLine(0)
            Log.d("NameLatLong", "Lat and Long is: $latLng")
            Log.d("NameLatLong", "Address 2 is : $knownName")

        } else {
            Log.d("Location_", "No address found")
        }

    } catch (e: Exception) {

        Log.e("Location_", "Error: ${e.message}")
    }


    //geocoder.getFromLocation(latitude, longitude, 1, listener)
    println("Latitude_: $latitude, Longitude_: $longitude")
    Log.d("Location_", "Known name: $knownName")
    return knownName
}


/*Function that opens the map ....*/
fun openGoogleMaps(locationData: String, context: Context) {

    val parts = locationData.split(", ")

    Log.d("Location", "is $locationData")

    val latitude = parts[0].substringAfter("Lat: ").toDouble()
    val longitude = parts[1].substringAfter("Long: ").toDouble()

    println("Latitude: $latitude, Longitude: $longitude")

    Log.d("onGoogleMaps2", "Latitude: $latitude, Longitude: $longitude")

    if (latitude != null && longitude != null) {

        val geoUri = Uri.parse("geo:0,0?q=$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            val webUri = Uri.parse("http://maps.google.com/maps?q=$latitude,$longitude")
            val webIntent = Intent(Intent.ACTION_VIEW, webUri)
            webIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(webIntent)
        }

    } else {
        Toast.makeText(context, "No app or browser found to open Google Maps", Toast.LENGTH_SHORT)
            .show()
    }

}
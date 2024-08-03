package com.example.expensestrackercomposablev2.appComponents

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensestrackercomposablev2.viewModels.MainViewModel


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ShowDialog(
    context: Context,
    viewModel: MainViewModel
) {

    val location = viewModel.itemDetails!!.location

    Log.d("LocationComing: ", "is $location")


    val addressText = getAddressFromLatLng(
        location,
        LocalContext.current
    )

    Log.d("AddressName: ", "is $addressText")

    Column(
        modifier = Modifier
            .width(400.dp)
            .height(300.dp) // 500
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(15.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Details",
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp
            )
        }

        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
        )

        viewModel.itemDetails.let { item ->

            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {

                //Name
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Name:",
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    Text(
                        text = item!!.itemName
                    )
                }

                //Price
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Price:"
                    )
                    Text(
                        text = "${item!!.quantity} THB"
                    )
                }

                //Date purchase
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Date:"
                    )
                    Text(
                        text = item!!.datePurchase
                    )
                }

                //Location
                Text(
                    text = "Location \n${addressText}",
                    modifier = Modifier.padding(top = 10.dp)
                )

            }
        }

        //Component to view the map
        //MapComponent(location)

        Button(
            onClick = {
                openGoogleMaps(
                    locationData = location,
                    context = context
                )
            }, //  viewModel::deleteItem
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
            modifier = Modifier.padding(top = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Open in map")
            }// Delete

        }


    }
}

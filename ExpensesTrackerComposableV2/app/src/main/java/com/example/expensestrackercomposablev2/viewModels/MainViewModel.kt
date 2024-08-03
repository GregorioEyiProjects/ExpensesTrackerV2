package com.example.expensestrackercomposablev2.viewModels

import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensestrackercomposablev2.MyApp
import com.example.expensestrackercomposablev2.models.ItemPurchase
import com.example.expensestrackercomposablev2.models.User
import com.example.expensestrackercomposablev2.ui.theme.blue
import com.example.expensestrackercomposablev2.ui.theme.blueGray
import com.example.expensestrackercomposablev2.ui.theme.brightBlue
import com.example.expensestrackercomposablev2.ui.theme.darkBlue
import com.example.expensestrackercomposablev2.ui.theme.green
import com.example.expensestrackercomposablev2.ui.theme.nightDark
import com.example.expensestrackercomposablev2.ui.theme.orange
import com.example.expensestrackercomposablev2.ui.theme.redOrange
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.query.RealmQuery
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class MainViewModel : ViewModel() {

    /*Variables*/

    private val realmDB = MyApp.realm

    private var getAllItems = realmDB.query<ItemPurchase>()
    var todayItemsComing: StateFlow<List<ItemPurchase>> =  MutableStateFlow<List<ItemPurchase>>(emptyList())
    var thisWeekItemsComing: StateFlow<List<ItemPurchase>> = MutableStateFlow<List<ItemPurchase>>(emptyList())
    var thisMonthItemsComing: StateFlow<List<ItemPurchase>> =  MutableStateFlow<List<ItemPurchase>>(emptyList())
    var customItemsComing: StateFlow<List<ItemPurchase>> =  MutableStateFlow<List<ItemPurchase>>(emptyList())

    private var onFilterAppliedCallback: (String) -> Unit = {}


    // it will react to the changes we made in the database
    val allItems = realmDB
        .query<ItemPurchase>()
        .asFlow()
        .map { result ->
            result.list.toList()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )


    var itemDetails: ItemPurchase? by mutableStateOf(null) // to get a single item details
        private set


    /*Functions*/

    @RequiresApi(Build.VERSION_CODES.O)
    fun filterExpensesByTimePeriod(timePeriod: String) {

        Log.d("DataFormat", "time period: $timePeriod")

        when (timePeriod) {
            "Today" -> {

                val startOfDay = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }.time

                val endOfDay = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 59)
                    set(Calendar.SECOND, 59)
                }.time

                Log.d("DataFormat", "before parse $startOfDay.toString()")
                Log.d("DataFormat", "before parse $endOfDay.toString()")
                val startDay = formatDate(startOfDay)
                val endDay = formatDate(endOfDay)
                Log.d("DataFormat", "startDay $startDay")
                Log.d("DataFormat", "endDay $endDay")


                //Query
                /*val result = realmDB.query<ItemPurchase>(
                    "datePurchase >= $0 AND datePurchase <= $1",
                    startDay,
                    endDay
                ).find()*/
                val result = realmDB.query<ItemPurchase>(
                    "datePurchase >= $0",
                    startDay,
                    "datePurchase <= $1",
                    endDay
                ).find()


                todayItemsComing = result.asFlow().map { res ->
                    res.list.toList()
                }.stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(),
                    emptyList()
                )

            }


            "This week" -> {
                Log.d("DataFormat", "Option: This week")
                val startOfWeek = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }.time

                Log.d("DataFormat ", "before parse $startOfWeek.toString()")
                val startWeek = formatDate(startOfWeek)
                Log.d("DataFormat", "startDay $startWeek")


                //Query 1
                //val realResult = realmDB.query<ItemPurchase>("datePurchase == $0", startWeek).find()
                val realResult = realmDB.query<ItemPurchase>("datePurchase >= $0", startWeek).find()

                //Query 2, to convert RealmResults to StateFlow

                thisWeekItemsComing = realResult.asFlow().map { result ->
                    result.list.toList()
                }.stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(),
                    emptyList()
                )

                //printItems(thisWeekItemsComing)


                Log.i("ITEMS_FOR_THIS_WEEK", thisWeekItemsComing.toString())

                //onFilterAppliedCallback(timePeriod)


                Log.d("ThisWeekItems", startOfWeek.toString())

            }

            "This month" -> {

                Log.d("DataFormat", "Option: This Month")

                val startOfMonth = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }.time


                Log.d("DataFormat ", "before parse $startOfMonth.toString()")
                val startMonth = formatDate(startOfMonth)
                Log.d("DataFormat ", "startDay $startMonth")

                //Query
                val result =  realmDB.query<ItemPurchase>("datePurchase >= $0", startMonth).find()
                thisMonthItemsComing = result.asFlow().map {rest->
                    rest.list.toList()
                }.stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(),
                    emptyList()
                )

            }

            "Custom" -> {
                Log.d("DataFormat", "Option: Custom")

                val startDayString = "27-07-2024 00:00:00 AM" // has to be dynamic
                val endDayString = "27-07-2024 11:59:00 PM" // has to be dynamic
                /*val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.ENGLISH)
                val startDay = dateFormat.parse(startDayString)
                val endDay = dateFormat.parse(endDayString)*/

                //Query
                   val result = realmDB.query<ItemPurchase>(
                       "datePurchase >= $0",
                       startDayString,
                       "datePurchase <= $1",
                       endDayString
                   )
                customItemsComing = result.asFlow().map {resu->
                    resu.list.toList()
                }.stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(),
                    emptyList()
                )

            }

            else -> {
                getAllItems.find() // All expenses
            }
        }

    }

    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss a", Locale.ENGLISH) // hh -> 12 h and HH -> HH
        return sdf.format(date)
    }

    fun setOnFilterAppliedListener(listener: (String) -> Unit) {
        onFilterAppliedCallback = listener
    }

    /*Adding items to the Realm DB*/
    fun addItemToRealmDB(
        itemQuantity: Double,
        itemName_: String,
        itemDate_: String,
        itemLocation_: String
    ) {
        viewModelScope.launch {
            realmDB.write {
                /*Change this one when i am gonna have the login and registration page */
                val userByDefaultForNow = User().apply {
                    userName = "Gregorio"
                    userGmail = "Gregorio@gmail.com"
                    userPassword = "password"
                }

                val item = ItemPurchase().apply {
                    itemName = itemName_
                    quantity = itemQuantity
                    datePurchase = itemDate_
                    location = itemLocation_
                    user = userByDefaultForNow
                }

                userByDefaultForNow.itemPurchase = realmListOf(item)

                copyToRealm(item, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(userByDefaultForNow, updatePolicy = UpdatePolicy.ALL)

            }
        }
    }

    //Function 3
    fun getItemDetails(itemPurchase: ItemPurchase) {
        itemDetails = itemPurchase
    }

    //Function 4
    fun hideItemsDetails() {
        itemDetails = null
    }

    //Function 5
    fun deleteItem() {

        viewModelScope.launch {
            realmDB.write {
                val item = itemDetails ?: return@write
                val latestItem = findLatest(item) ?: return@write
                delete(latestItem) // deleteAll()
                itemDetails = null // to hide the Dialog
            }
        }
    }

    //
    private val colors = listOf(
        darkBlue, // Add your desired theme colors here
        orange,
        blueGray,
        nightDark,
        redOrange,
        green,
        blue,
        brightBlue
    )

    private val _randomColors = mutableListOf<Color>()
    val randomColors: List<Color>
        get() = _randomColors


}


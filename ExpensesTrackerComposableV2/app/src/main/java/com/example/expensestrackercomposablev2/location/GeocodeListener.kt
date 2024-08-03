package com.example.expensestrackercomposablev2.location

import android.location.Address

interface GeocodeListener {
    fun onAddressFound(addresses: List<Address>?)
    fun onError(errorMessage: String)
}
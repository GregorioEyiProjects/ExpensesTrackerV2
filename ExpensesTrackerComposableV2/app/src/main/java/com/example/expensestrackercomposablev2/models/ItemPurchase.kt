package com.example.expensestrackercomposablev2.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class ItemPurchase:RealmObject { //
    @PrimaryKey var _itemID: ObjectId = ObjectId()
    var itemName: String = ""
    var quantity: Double = 0.0
    var datePurchase: String = ""
    var location: String = ""
    var user: User? = null
}

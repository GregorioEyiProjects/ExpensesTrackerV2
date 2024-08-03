package com.example.expensestrackercomposablev2.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class User:RealmObject{
    @PrimaryKey var userID:ObjectId = ObjectId()
    var userName:String = ""
    var userGmail:String = ""
    var userPassword:String = ""
    var itemPurchase: RealmList<ItemPurchase> = realmListOf()
}



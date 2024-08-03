package com.example.expensestrackercomposablev2

import android.app.Application
import com.example.expensestrackercomposablev2.models.ItemPurchase
import com.example.expensestrackercomposablev2.models.User
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class MyApp:Application(){

    companion object{
        lateinit var realm:Realm
    }

    //Realm DB configuration
    override fun onCreate() {
        super.onCreate()
        realm = Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(
                    User::class,
                    ItemPurchase::class
                )
            )
        )
    }

}
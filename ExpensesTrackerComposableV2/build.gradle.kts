// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false

    //TO use RealmDB
    id ("io.realm.kotlin") version "1.16.0" apply false


}
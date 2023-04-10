package com.example.platepat.utils

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Meal::class], version=1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): MealDAO
}
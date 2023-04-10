package com.example.platepat.utils

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MealDAO {
    @Query("Select * from meals")
    suspend fun getAll(): List<Meal>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(vararg meal: Meal)
    @Insert
    suspend fun insertAll(vararg meal: Meal)
}
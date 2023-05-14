package com.example.meal_preparation_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.example.meal_preparation_application.R
import com.example.meal_preparation_application.classes.AppDatabase
import com.example.meal_preparation_application.classes.Meals
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class searchMeal : AppCompatActivity() {
    lateinit var Meals: List<Meals>
    lateinit var selected_meals: ArrayList<Meals>;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_meal)

        selected_meals = ArrayList()

        var test="egg"

        val db = Room.databaseBuilder(
            this, AppDatabase::class.java,
            "mealdatabase"
        ).build()
        val mealDao = db.mealDao()
        runBlocking {
            launch {
                Meals = mealDao.getAll()
            }

//            val names = listOf(mealDao)
//            val query = readLine()?.toLowerCase() ?: ""
//
//            val filteredNames = names.filter { it.toLowerCase().contains(query) }
//            println("Results: ${filteredNames.joinToString(", ")}")

        }

        var nameBaseMeals = ArrayList<Meals>()
        var ingBaseMeals = ArrayList<Meals>()

        for (meal in Meals){
            if (meal.name?.contains(test, ignoreCase = true) == true){
                nameBaseMeals.add(meal)
            }

            for (ing in meal.ingredients!!){
                if (ing.contains(test, ignoreCase = true) == true){
                    ingBaseMeals.add(meal)
                    break
                }
            }

            selected_meals.addAll(nameBaseMeals)
            selected_meals.addAll(ingBaseMeals)

            selected_meals = ArrayList(selected_meals.distinct())

            println(selected_meals)
        }
    }
}

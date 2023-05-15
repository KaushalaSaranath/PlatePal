package com.example.meal_preparation_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import androidx.room.Room
import com.example.meal_preparation_application.R
import com.example.meal_preparation_application.classes.AppDatabase
import com.example.meal_preparation_application.classes.Meals
import com.gtappdevelopers.kotlingfgproject.SaveMeal
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class searchMeal : AppCompatActivity() {
    lateinit var Meals: List<Meals>
    lateinit var selected_meals: ArrayList<Meals>
    lateinit var loadMealEditText: EditText

    lateinit var courseGRV: GridView
    lateinit var courseList: List<saveMealsCardView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_meal)

        selected_meals = ArrayList()

        // initializing variables of grid view with their ids.
        courseGRV = findViewById(R.id.idGRV)
        courseList = ArrayList<saveMealsCardView>()

        loadMealEditText  = findViewById(R.id.seachmealId)
        var loadMealButton : Button = findViewById(R.id.textboxID)

        val db = Room.databaseBuilder(
            this, AppDatabase::class.java,
            "mealdatabase"
        ).build()
        val mealDao = db.mealDao()
        runBlocking {
            launch {
                Meals = mealDao.getAll()
            }

        }

        loadMealButton.setOnClickListener {
            SaveMealView()
        }


    }

    private fun SaveMealView() {
        selected_meals.clear()
        var nameBaseMeals = ArrayList<Meals>()
        var ingBaseMeals = ArrayList<Meals>()

        for (meal in Meals){
            if (meal.name?.contains(loadMealEditText.text, ignoreCase = true) == true){
                nameBaseMeals.add(meal)
            }

            for (ing in meal.ingredients!!){
                if (ing.contains(loadMealEditText.text, ignoreCase = true)){
                    ingBaseMeals.add(meal)
                    break
                }
            }
        }
        selected_meals.addAll(nameBaseMeals)
        selected_meals.addAll(ingBaseMeals)

        selected_meals = ArrayList(selected_meals.distinct())

        // on below line we are adding data to
        // our course list with image and course name.
        for (meal in selected_meals){
            courseList = courseList + saveMealsCardView(meal)
        }

        // on below line we are initializing our course adapter
        // and passing course list and context.
        val courseAdapter = SaveMeal(courseList = courseList, this@searchMeal)

        // on below line we are setting adapter to our grid view.
        courseGRV.adapter = courseAdapter
    }

    // Save data to restore later
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }
    // Retrieve saved data
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Use the restored data as needed
        SaveMealView()

    }
}

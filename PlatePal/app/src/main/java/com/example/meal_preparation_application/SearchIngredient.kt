package com.example.meal_preparation_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.meal_preparation_application.classes.Meals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchIngredient : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_ingredient)

        var stb = StringBuilder()

        val url_string = "https://www.themealdb.com/api/json/v1/1/search.php?s=chicken"
        val url = URL(url_string)
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
        val RetriveMeal = findViewById<Button>(R.id.retrieveId)

        RetriveMeal.setOnClickListener{
            runBlocking {
                launch {
                    // run the code of the coroutine in a new thread
                    withContext(Dispatchers.IO) {
                        var bf = BufferedReader(InputStreamReader(con.inputStream))
                        var line: String? = bf.readLine()
                        while (line != null) {
                            stb.append(line + "\n")
                            line = bf.readLine()
                        }
                        parseJSON(stb)
                    }

                }
            }
        }

    }suspend fun parseJSON(stb: java.lang.StringBuilder) {
// this contains the full JSON returned by the Web Service
        val json = JSONObject(stb.toString())
// Information about all the books extracted by this function
        var allMeals = ArrayList<Meals>()
        var jsonArray: JSONArray = json.getJSONArray("meals")
// extract all the books from the JSON array
        for (i in 0..jsonArray.length()-1) {
            val food: JSONObject = jsonArray[i] as JSONObject // this is a json object
// extract the title

            val tempMeal = Meals(
                name = food["strMeal"] as? String ?: null,
                drinkAlternate = food["strDrinkAlternate"] as? String ?: null,
                category = food["strCategory"] as? String ?: null,
                area = food["strArea"]as? String ?: null,
                instructions = food["strInstructions"] as? String ?: null,
                mealThumb = food["strMealThumb"] as? String ?: null,
                ingredients = getList(food,"strIngredient"),
                measure = getList(food,"strMeasure"),
                tags = food["strTags"] as? String ?: null,
                youtube = food["strYoutube"] as? String ?: null,
                source = food["strSource"] as? String ?: null,
                imageSource = food["strImageSource"] as? String ?: null,
                creativeCommonsConfirmed = food["strCreativeCommonsConfirmed"] as? String ?: null,
                dateModified = food["dateModified"] as? String ?: null,
            )
            allMeals.add(tempMeal)
// extract all the authors
        }
        println("data stored")
        println(allMeals)
    }

    //
    private fun getList(jsonMealList: JSONObject, typeName: String): ArrayList<String> {
        val temp = ArrayList<String>()
        for (i in 1..20) {
            val type = jsonMealList[typeName+i.toString()]as? String ?: null
            temp.add(type.toString())
        }
        return temp;
    }
//    val search: Button = findViewById(R.id.retrieveId)
//    search.setOnClickListener {}
}

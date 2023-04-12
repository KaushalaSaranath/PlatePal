package com.example.meal_preparation_application

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
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

    lateinit var scroll_linearLayout : LinearLayout
    var allMeals: ArrayList<Meals>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_ingredient)

        var stb = StringBuilder()

        val RetriveMeal = findViewById<Button>(R.id.retrieveId)
        val search_field = findViewById<EditText>(R.id.searchId)
        scroll_linearLayout = findViewById<LinearLayout>(R.id.scroll_layout)

        RetriveMeal.setOnClickListener{
            val url_string = "https://www.themealdb.com/api/json/v1/1/search.php?s="+ search_field.text
            val url = URL(url_string)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
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
                        runOnUiThread{
                            createMealCards()
                        }
                    }

                }
            }
        }

    }

    private fun createMealCards() {
        for (i in 1..(allMeals?.size!!)) {

            val cardView = CardView(this)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(25, 15, 25, 0)
            cardView.layoutParams = layoutParams
            cardView.cardElevation = 30f
            cardView.radius = 35f

            // Create the child views for the CardView
            val linearLayout = LinearLayout(this)
            linearLayout.orientation = LinearLayout.VERTICAL
            linearLayout.setPadding(15, 25, 15, 0)

            val imageView = ImageView(this)
            imageView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                460
            )
            imageView.setPadding(5, 30, 5, 0)
            imageView.setImageResource(R.drawable.testfood)
            linearLayout.addView(imageView)

            val dishNameTextView = TextView(this)
            dishNameTextView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            dishNameTextView.text = "TextView"
            dishNameTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            dishNameTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            dishNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            dishNameTextView.setTypeface(Typeface.DEFAULT_BOLD)

            linearLayout.addView(dishNameTextView)

            val categoryTextView = TextView(this)
            categoryTextView.text = "Category : "
            categoryTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            categoryTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            categoryTextView.setPadding(5, 10, 0, 0)

            linearLayout.addView(categoryTextView)

            val areaTextView = TextView(this)
            areaTextView.text = "Area : "
            areaTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            areaTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            areaTextView.setPadding(5, 10, 0, 0)

            linearLayout.addView(areaTextView)

            val tagsTextView = TextView(this)
            tagsTextView.text = "Tags : "
            tagsTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            tagsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            tagsTextView.setPadding(5, 10, 0, 0)

            linearLayout.addView(tagsTextView)

            val drinkAlternateTextView = TextView(this)
            drinkAlternateTextView.text = "Drink Alternate : "
            drinkAlternateTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            drinkAlternateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            drinkAlternateTextView.setPadding(5, 10, 0, 0)

            linearLayout.addView(drinkAlternateTextView)

            val InstructionTextView = TextView(this)
            InstructionTextView.text = "Instructions : "
            InstructionTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            InstructionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            InstructionTextView.setPadding(5, 10, 0, 0)

            linearLayout.addView(InstructionTextView)



            val sourceTextView = TextView(this)
            sourceTextView.text = "Source : "
            sourceTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            sourceTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            sourceTextView.setPadding(5, 10, 0, 0)

            linearLayout.addView(sourceTextView)

            val youtubeTextView = TextView(this)
            youtubeTextView.text = "YouTube : "
            youtubeTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            youtubeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            youtubeTextView.setPadding(5, 10, 0, 0)

            linearLayout.addView(youtubeTextView)

            val imageSourceTextView = TextView(this)
            imageSourceTextView.text = "Image Source : "
            imageSourceTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            imageSourceTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            imageSourceTextView.setPadding(5, 10, 0, 0)

            linearLayout.addView(imageSourceTextView)


            val creativeCommonsConfirmedTextView = TextView(this)
            creativeCommonsConfirmedTextView.text = "Creative CommonsConfirmed : "
            creativeCommonsConfirmedTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            creativeCommonsConfirmedTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            creativeCommonsConfirmedTextView.setPadding(5, 10, 0, 0)

            linearLayout.addView(creativeCommonsConfirmedTextView)

            val dateModifiedTextView = TextView(this)
            dateModifiedTextView.text = "Date Modified : "
            dateModifiedTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            dateModifiedTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            dateModifiedTextView.setPadding(5, 10, 0, 0)

            linearLayout.addView(dateModifiedTextView)
//
//
//            val instructionsTextView = TextView(this)
//            instructionsTextView.layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            instructionsTextView.text = "Instructions"
//            instructionsTextView.setTextColor(Color.parseColor("#000000"))
//            instructionsTextView.setTypeface(Typeface.DEFAULT, Typeface.ITALIC)
//
//            linearLayout.addView(instructionsTextView)
//
//            val cookingTextView = TextView(this)
//            cookingTextView.layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            cookingTextView.text = "cooking meal"
//            cookingTextView.setTextColor(Color.parseColor("#000000"))
//            cookingTextView.setTypeface(Typeface.DEFAULT)
//
//            linearLayout.addView(cookingTextView)

//            val measureTextView = TextView(this)
//            measureTextView.layoutParams = LinearLayout.LayoutParams(
//                154,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            measureTextView.text = "100g"
//            measureTextView.setTextColor(Color.parseColor("#000000"))
//            measureTextView.setTypeface(Typeface.DEFAULT, Typeface.ITALIC)
            cardView.addView(linearLayout)
            scroll_linearLayout.addView(cardView)
        }
    }

    suspend fun parseJSON(stb: java.lang.StringBuilder) {
// this contains the full JSON returned by the Web Service
        val json = JSONObject(stb.toString())
// Information about all the books extracted by this function
        allMeals = ArrayList<Meals>()
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
            allMeals!!.add(tempMeal)
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

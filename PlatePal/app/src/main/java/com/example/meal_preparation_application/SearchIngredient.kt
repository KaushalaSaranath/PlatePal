package com.example.meal_preparation_application

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.meal_preparation_application.classes.AppDatabase
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
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_ingredient)

        val db = Room.databaseBuilder(this, AppDatabase::class.java,
            "mealdatabase").build()
        val mealDao = db.mealDao()



        val RetriveMeal = findViewById<Button>(R.id.retrieveId)
        val search_field = findViewById<EditText>(R.id.searchId)
        val saveMeals = findViewById<Button>(R.id.savemealid)
        scroll_linearLayout = findViewById<LinearLayout>(R.id.scroll_layout)

        saveMeals.setOnClickListener{
            runBlocking {
                launch {
                    for (index in 0 until (allMeals?.size !!)){
                        mealDao.insert(allMeals?.get(index) !!)
                    }

                }
            }
        }





        RetriveMeal.setOnClickListener{
            // collecting all the JSON string
            var stb = StringBuilder()
            val url_string = "https://www.themealdb.com/api/json/v1/1/filter.php?i="+ search_field.text
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
                            scroll_linearLayout.removeAllViews()
                            createMealCards()
                        }
                    }
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createMealCards() {
        for (i in 0 until (allMeals?.size!!)) {
            var iscard_span = false

            val cardView = CardView(this)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(55, 65, 55, 0)
            cardView.layoutParams = layoutParams
            cardView.cardElevation = 30f
            cardView.radius = 45f

            // Create the child views for the CardView
            val linearLayout = LinearLayout(this)
            linearLayout.orientation = LinearLayout.VERTICAL
            linearLayout.setPadding(25, 25, 25, 30)
            linearLayout.setBackgroundColor(Color.parseColor("#FFFFF1"))

            //get image url to a image View


            var imageView = ImageView(this)
            imageView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                800
            ).apply {
                height = 800
                width = 800
                gravity = Gravity.CENTER
            }
            imageView.setPadding(5, 30, 5, 0)
            Glide.with(this)
                .load(allMeals!![i].mealThumb)
                .into(imageView)


            linearLayout.addView(imageView)

            val dishNameTextView = TextView(this)
            dishNameTextView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 80
                bottomMargin = 15
            }
            dishNameTextView.text = allMeals!![i].name
            dishNameTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            dishNameTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            dishNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
            dishNameTextView.setTypeface(Typeface.DEFAULT_BOLD)

            linearLayout.addView(dishNameTextView)

            val categoryTextView = TextView(this)
            categoryTextView.text = "Category : "+ allMeals!![i].category
            categoryTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            categoryTextView.setTypeface(Typeface.DEFAULT_BOLD)
            categoryTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            categoryTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            categoryTextView.setPadding(5, 10, 0, 0)

            linearLayout.addView(categoryTextView)

            val areaTextView = TextView(this)
            areaTextView.text = "Area : " + allMeals!![i].area
            areaTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            areaTextView.setTypeface(Typeface.DEFAULT_BOLD)
            areaTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            areaTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            areaTextView.setPadding(5, 10, 0, 0)

            linearLayout.addView(areaTextView)


            var tagsTextView: TextView? = null
            if (allMeals!![i].tags !=null){
                tagsTextView = TextView(this)
                tagsTextView.text = "Tags : " + allMeals!![i].tags
                tagsTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
                tagsTextView.setTypeface(Typeface.DEFAULT_BOLD)
                tagsTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
                tagsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                tagsTextView.setPadding(5, 10, 0, 0)

                linearLayout.addView(tagsTextView)
            }

            var drinkAlternateTextView: TextView? = null
            if (allMeals!![i].drinkAlternate !=null){
                drinkAlternateTextView = TextView(this)
                drinkAlternateTextView.text = "Drink Alternate : " + allMeals!![i].drinkAlternate
                drinkAlternateTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
                drinkAlternateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                drinkAlternateTextView.setPadding(5, 10, 0, 0)
                drinkAlternateTextView.isVisible = false

                linearLayout.addView(drinkAlternateTextView)
            }


            val InstructionTextView = TextView(this)
            InstructionTextView.text = "Instructions : "
            InstructionTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            InstructionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            InstructionTextView.setPadding(5, 10, 0, 0)
            InstructionTextView.isVisible = false

            linearLayout.addView(InstructionTextView)

            val InstructionDetailsTextView = TextView(this)
            InstructionDetailsTextView.text = allMeals!![i].instructions
            InstructionDetailsTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            InstructionDetailsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            InstructionDetailsTextView.justificationMode = JUSTIFICATION_MODE_INTER_WORD
            InstructionDetailsTextView.setPadding(5, 10, 0, 0)
            InstructionDetailsTextView.isVisible = false

            linearLayout.addView(InstructionDetailsTextView)

            var ingredientTextView: TextView? = null
            var IngredientsDetailsTextView: TextView? = null
            if (allMeals!![i].ingredients !=null){
                ingredientTextView = TextView(this)
                ingredientTextView.text = "Ingredients"
                ingredientTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
                ingredientTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                ingredientTextView.setPadding(5, 10, 0, 0)
                ingredientTextView.isVisible = false

                linearLayout.addView(ingredientTextView)
                val ingredients =  allMeals!![i].ingredients
                val measure = allMeals!![i].measure
                val formattedIngredients = ingredients?.zip(measure as java.util.ArrayList)
                    ?.filter { it.second.isNotBlank() }
                    ?.joinToString(separator = ", ") { "${it.first} - ${it.second}" }

                IngredientsDetailsTextView = TextView(this)
                IngredientsDetailsTextView.text = formattedIngredients
                IngredientsDetailsTextView.justificationMode = JUSTIFICATION_MODE_INTER_WORD
                IngredientsDetailsTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
                IngredientsDetailsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                IngredientsDetailsTextView.setPadding(5, 10, 0, 0)
                IngredientsDetailsTextView.isVisible = false

                linearLayout.addView(IngredientsDetailsTextView)
            }


            var sourceTextView: TextView? = null
            if (allMeals!![i].source !=null){
                sourceTextView = TextView(this)
                sourceTextView.text = "Source : "
                sourceTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
                sourceTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                sourceTextView.setPadding(5, 10, 0, 0)
                sourceTextView.isVisible = false

                linearLayout.addView(sourceTextView)
            }

            var sourceDetailsTextView: TextView? = null
            if (allMeals!![i].source !=null){
                sourceDetailsTextView = TextView(this)
                sourceDetailsTextView.text = allMeals!![i].source
                sourceDetailsTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
                sourceDetailsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                sourceDetailsTextView.setPadding(5, 10, 0, 0)
                sourceDetailsTextView.isVisible = false

                linearLayout.addView(sourceDetailsTextView)

            }

            var youtubeTextView: TextView? = null
            if (allMeals!![i].youtube !=null){
                youtubeTextView = TextView(this)
                youtubeTextView.text = "YouTube : "
                youtubeTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
                youtubeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                youtubeTextView.setPadding(5, 10, 0, 0)
                youtubeTextView.isVisible = false

                linearLayout.addView(youtubeTextView)

            }

            var youtubeDetailTextView: TextView? = null
            if (allMeals!![i].youtube !=null){
                youtubeDetailTextView = TextView(this)
                youtubeDetailTextView.text = allMeals!![i].youtube
                youtubeDetailTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
                youtubeDetailTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                youtubeDetailTextView.setPadding(5, 10, 0, 0)
                youtubeDetailTextView.isVisible = false

                linearLayout.addView(youtubeDetailTextView)
            }

            var imageSourceTextView: TextView? = null
            if (allMeals!![i].imageSource !=null){
                imageSourceTextView = TextView(this)
                imageSourceTextView.text = "Image Source : "
                imageSourceTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
                imageSourceTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                imageSourceTextView.setPadding(5, 10, 0, 0)
                imageSourceTextView.isVisible = false

                linearLayout.addView(imageSourceTextView)
            }

            var imageSourceDetailsTextView: TextView? = null
            if (allMeals!![i].imageSource !=null){
                imageSourceDetailsTextView = TextView(this)
                imageSourceDetailsTextView.text = allMeals!![i].imageSource
                imageSourceDetailsTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
                imageSourceDetailsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                imageSourceDetailsTextView.setPadding(5, 10, 0, 0)
                imageSourceDetailsTextView.isVisible = false

                linearLayout.addView(imageSourceDetailsTextView)
            }


            var creativeCommonsConfirmedTextView: TextView? = null
            if (allMeals!![i].creativeCommonsConfirmed !=null){
                creativeCommonsConfirmedTextView = TextView(this)
                creativeCommonsConfirmedTextView.text = "Creative CommonsConfirmed : " + allMeals!![i].creativeCommonsConfirmed
                creativeCommonsConfirmedTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
                creativeCommonsConfirmedTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                creativeCommonsConfirmedTextView.setPadding(5, 10, 0, 0)
                creativeCommonsConfirmedTextView.isVisible = false

                linearLayout.addView(creativeCommonsConfirmedTextView)

            }

            var dateModifiedTextView: TextView? = null
            if (allMeals!![i].dateModified !=null){
                dateModifiedTextView = TextView(this)
                dateModifiedTextView.text = "Date Modified : " + allMeals!![i].dateModified
                dateModifiedTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
                dateModifiedTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                dateModifiedTextView.setPadding(5, 10, 0, 40)
                dateModifiedTextView.isVisible = false

                linearLayout.addView(dateModifiedTextView)
            }

            linearLayout.setOnClickListener {
                iscard_span = !iscard_span
                if (drinkAlternateTextView != null) {
                    drinkAlternateTextView.isVisible = iscard_span
                }
                InstructionTextView.isVisible = iscard_span
                InstructionDetailsTextView.isVisible = iscard_span
                if (sourceTextView != null) {
                    sourceTextView.isVisible = iscard_span
                }
                if (sourceDetailsTextView != null) {
                    sourceDetailsTextView.isVisible = iscard_span
                }
                if (youtubeTextView != null) {
                    youtubeTextView.isVisible = iscard_span
                }
                if (youtubeDetailTextView != null) {
                    youtubeDetailTextView.isVisible = iscard_span
                }
                if (imageSourceTextView != null) {
                    imageSourceTextView.isVisible = iscard_span
                }
                if (imageSourceTextView != null) {
                    imageSourceTextView.isVisible = iscard_span
                }
                if (creativeCommonsConfirmedTextView != null) {
                    creativeCommonsConfirmedTextView.isVisible = iscard_span
                }
                if (dateModifiedTextView != null) {
                    dateModifiedTextView.isVisible = iscard_span
                }
                if (ingredientTextView != null) {
                    ingredientTextView.isVisible = iscard_span
                }
                if (IngredientsDetailsTextView != null) {
                    IngredientsDetailsTextView.isVisible = iscard_span
                }

            }
            cardView.addView(linearLayout)
            scroll_linearLayout.addView(cardView)
        }
    }

    suspend fun parseJSON(stb: java.lang.StringBuilder) {
        println(stb)
        // this contains the full JSON returned by the Web Service
        val json = JSONObject(stb.toString())
        // Information about all the books extracted by this function
        allMeals = ArrayList<Meals>()
        var jsonArray: JSONArray = json.getJSONArray("meals")
        // extract all the books from the JSON array
        for (i in 0..jsonArray.length()-1) {
            val food: JSONObject = jsonArray[i] as JSONObject // this is a json object
        // extract the title
            // collecting all the JSON string
            var stb = StringBuilder()
            val url_string = "https://www.themealdb.com/api/json/v1/1/lookup.php?i="+ food["idMeal"]
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
                        // this contains the full JSON returned by the Web Service
                        val json = JSONObject(stb.toString())
                        // Information about all the books extracted by this function
                        var jsonArray: JSONArray = json.getJSONArray("meals")
                        // extract all the books from the JSON array
                        println(jsonArray.length())

                        for (i in 0..jsonArray.length() - 1) {
                            val food: JSONObject =
                                jsonArray[i] as JSONObject // this is a json object
                            val tempMeal = Meals(
                                name = food["strMeal"] as? String ?: null,
                                drinkAlternate = food["strDrinkAlternate"] as? String ?: null,
                                category = food["strCategory"] as? String ?: null,
                                area = food["strArea"] as? String ?: null,
                                instructions = food["strInstructions"] as? String ?: null,
                                mealThumb = food["strMealThumb"] as? String ?: null,
                                ingredients = getList(food, "strIngredient"),
                                measure = getList(food, "strMeasure"),
                                tags = food["strTags"] as? String ?: null,
                                youtube = food["strYoutube"] as? String ?: null,
                                source = food["strSource"] as? String ?: null,
                                imageSource = food["strImageSource"] as? String ?: null,
                                creativeCommonsConfirmed = food["strCreativeCommonsConfirmed"] as? String
                                    ?: null,
                                dateModified = food["dateModified"] as? String ?: null,
                            )
                            allMeals!!.add(tempMeal)
                        }
                    }
                }
            }

        }
    }

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

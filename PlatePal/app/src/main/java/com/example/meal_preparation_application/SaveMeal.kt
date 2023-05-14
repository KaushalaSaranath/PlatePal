package com.gtappdevelopers.kotlingfgproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.meal_preparation_application.R
import com.example.meal_preparation_application.saveMealsCardView

// on below line we are creating an
// adapter class for our grid view.
internal class SaveMeal(
    // on below line we are creating two
    // variables for course list and context
    private val courseList: List<saveMealsCardView>,
    private val context: Context
) :
    BaseAdapter() {
    // in base adapter class we are creating variables
    // for layout inflater, course image view and course text view.
    private var layoutInflater: LayoutInflater? = null
    private lateinit var cardCategory: TextView
    private lateinit var cardName: TextView
    private lateinit var cardImageView: ImageView

    // below method is use to return the count of course list
    override fun getCount(): Int {
        return courseList.size
    }

    // below function is use to return the item of grid view.
    override fun getItem(position: Int): Any? {
        return null
    }

    // below function is use to return item id of grid view.
    override fun getItemId(position: Int): Long {
        return 0
    }

    // in below function we are getting individual item of grid view.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        // on blow line we are checking if layout inflater
        // is null, if it is null we are initializing it.
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        // on the below line we are checking if convert view is null.
        // If it is null we are initializing it.
        if (convertView == null) {
            // on below line we are passing the layout file
            // which we have to inflate for each item of grid view.
            convertView = layoutInflater!!.inflate(R.layout.save_meal_cards, null)
        }
        // on below line we are initializing our course image view
        // and course text view with their ids.
        cardName = convertView!!.findViewById(R.id.name)
        cardCategory = convertView.findViewById(R.id.category)
        cardImageView = convertView.findViewById(R.id.idIVCourse)

        cardName.text = courseList.get(position).Meal.name
        cardCategory.text = courseList.get(position).Meal.category

        Glide.with(convertView)
            .load(courseList.get(position).Meal.mealThumb)
            .into(cardImageView)


//        // on below line we are setting image for our course image view.
//        courseIV.setImageResource(courseList.get(position).courseImg)
//        // on below line we are setting text in our course text view.
//        courseTV.setText(courseList.get(position).courseName)
//        // at last we are returning our convert view.
        return convertView
    }
}

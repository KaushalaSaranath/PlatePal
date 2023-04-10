package com.example.platepat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.example.platepat.utils.AppDatabase
import com.example.platepat.utils.Meal
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create the database
        val db = Room.databaseBuilder(this, AppDatabase::class.java,
            "appdatabase").build()
        val userDao = db.userDao()

        runBlocking {
            launch {
                val user = User(1, "John", "Smith")
                val user2 = User(2, "Helen", "Jones")
                val user3 = User(3, "Mary", "Popkins")

                
                userDao.insertUsers(user, user2, user3)
                val meals: List<Meal> = userDao.getAll()

                println(meals)
            }
        }

    }
}
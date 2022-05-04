package au.edu.utas.kit721.strokerehab

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import au.edu.utas.kit721.strokerehab.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

const val FIREBASE_TAG = "FirebaseLogging"
const val EXERCISE_INDEX = "EXERCISE_Index"
const val FREE_MODE_KEY = "FREE_MODE"
const val CUSTOMIZATION_KEY : String = "CUSTOMIZATION"
const val REPETITION_KEY : String = "REPETITION"
const val PREFERENCE_FILE = "nameFile"
const val USERNAME_KEY = "username"
class MainActivity : AppCompatActivity() {

    private lateinit var ui: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        val navView: BottomNavigationView = ui.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        //get db connection
//        val db = Firebase.firestore
//
//
//        //get all movies
//        ui.lblMovieCount.text = "Loading..."
//        moviesCollection
//            .get()
//            .addOnSuccessListener { result ->
//                items.clear() //this line clears the list, and prevents a bug where items would be duplicated upon rotation of screen
//                Log.d(FIREBASE_TAG, "--- all movies ---")
//                for (document in result)
//                {
//                    //Log.d(FIREBASE_TAG, document.toString())
//                    val movie = document.toObject<Movie>()
//                    movie.id = document.id
//                    Log.d(FIREBASE_TAG, movie.toString())
//
//                    items.add(movie)
//                }
//                (ui.myList.adapter as MovieAdapter).notifyDataSetChanged()
//            }
    }
}
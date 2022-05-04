package au.edu.utas.kit721.strokerehab.ui.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import au.edu.utas.kit721.strokerehab.FREE_MODE_KEY
import au.edu.utas.kit721.strokerehab.R
import au.edu.utas.kit721.strokerehab.databinding.ActivityExercisesDetailsBinding
import au.edu.utas.kit721.strokerehab.databinding.ActivityPrescribedModeBinding

class PrescribedModeActivity : AppCompatActivity() {
    private lateinit var ui : ActivityPrescribedModeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityPrescribedModeBinding.inflate(layoutInflater)
        setContentView(ui.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Game Mode"
        actionbar.setDisplayHomeAsUpEnabled(true)

        ui.btnSetGoal.setOnClickListener {
            val i = Intent(this, PrescribedGoalActivity::class.java)
            startActivity(i)
//            finish()
        }

        ui.btnStartFreeMode.setOnClickListener {
            val i = Intent(this, PrescribedCustomizationActivity::class.java)
            i.putExtra(FREE_MODE_KEY, true)
            startActivity(i)
//            finish()
        }



    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}


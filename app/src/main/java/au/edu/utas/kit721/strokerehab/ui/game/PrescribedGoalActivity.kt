package au.edu.utas.kit721.strokerehab.ui.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import au.edu.utas.kit721.strokerehab.REPETITION_KEY
import au.edu.utas.kit721.strokerehab.databinding.ActivityPrescribedGoalBinding
import com.google.android.material.button.MaterialButtonToggleGroup

class PrescribedGoalActivity : AppCompatActivity() {

    private lateinit var ui : ActivityPrescribedGoalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityPrescribedGoalBinding.inflate(layoutInflater)
        setContentView(ui.root)

        var repetitionNumber :Int = 1
        var timeLimitNumber : Int = 0
        var timeRangeArray : Array<String> = arrayOf(
            "30 s",
            "1 min",
            "1 min 30 s",
            "2 min",
            "2 min 30 s",
            "3 min",
            "3 min 30 s",
            "4 min",
            "4 min 30 s",
            "5 min 30 s",
            "5 min"
        )

        val actionbar = supportActionBar
        actionbar!!.title = "Game goal"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        // Toggle button
        val toggleGroup : MaterialButtonToggleGroup = ui.goalOptionToggle
        toggleGroup.addOnButtonCheckedListener (object :
        MaterialButtonToggleGroup.OnButtonCheckedListener {
            override fun onButtonChecked(
                group: MaterialButtonToggleGroup?,
                checkedId: Int,
                isChecked: Boolean,
            ) {
                if (isChecked) {

                }
            }
        }
        )

        ui.goalSelectRepetition.setOnClickListener {
            ui.goalTimeLimitPicker.visibility = View.GONE
            ui.numberPicker.visibility = View.VISIBLE
        }

        ui.goalSelectTime.setOnClickListener {
            ui.numberPicker.visibility = View.GONE
            ui.goalTimeLimitPicker.visibility = View.VISIBLE
        }

        // Repetition Number picker
        val numberPicker : NumberPicker = ui.numberPicker
        numberPicker.minValue = 1
        numberPicker.maxValue = 10


        numberPicker.setOnValueChangedListener { numberPicker, pre, next ->
            repetitionNumber = next
            ui.textViewGoal.text = "You will play ${repetitionNumber} round(s)"
        }

        // Time Limit picker
        val timeLimitPicker : NumberPicker = ui.goalTimeLimitPicker
        timeLimitPicker.minValue = 1
        timeLimitPicker.maxValue = timeRangeArray.size
        timeLimitPicker.displayedValues = timeRangeArray

        timeLimitPicker.setOnValueChangedListener { numberPicker, pre, next ->
            timeLimitNumber  = next * 30
            ui.textViewGoal.text = "You will play ${timeRangeArray[next-1]}"
        }


        // Confirm settings and start game
        ui.goalConfirm.setOnClickListener {
            val i = Intent(this, PrescribedCustomizationActivity::class.java)
            i.putExtra(REPETITION_KEY, repetitionNumber)
            startActivity(i)
        }


        ui.goalCancle.setOnClickListener {
            finish()
        }




//


//        val numberPickerDialog = numberPickerDialog.B
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
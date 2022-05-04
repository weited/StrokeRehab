package au.edu.utas.kit721.strokerehab.ui.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import au.edu.utas.kit721.strokerehab.*
import au.edu.utas.kit721.strokerehab.databinding.ActivityPrescribedCustomizationBinding

class PrescribedCustomizationActivity : AppCompatActivity() {

    var isBtnRandom : Boolean = true
    var isBtnIndicator : Boolean = true
    var btnNumber : Int? = 3
    var isFreeMode : Boolean = false
    var repetition : Int = 3
    var btnSize : Int = 1

    private lateinit var ui : ActivityPrescribedCustomizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityPrescribedCustomizationBinding.inflate(layoutInflater)
        setContentView(ui.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Game Customization"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        repetition = intent.getIntExtra(REPETITION_KEY,0)
        isFreeMode = intent.getBooleanExtra(FREE_MODE_KEY,false)

        val btnNumRange = arrayOf(2,3,4,5)
        val spinner = ui.spinnerBtnNum
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                R.layout.support_simple_spinner_dropdown_item,btnNumRange)
            spinner.adapter = adapter
            spinner.setSelection(1)
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?, position: Int, id: Long,
                ) {
                    btnNumber = btnNumRange[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        ui.btnSizeSlide.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, p2: Boolean) {
                when (progress) {
                    0 -> ui.textViewBtnSize.text = "Small"
                    1 -> ui.textViewBtnSize.text = "Normal"
                    2 -> ui.textViewBtnSize.text = "Large"
                    else -> ui.textViewBtnSize.text = "Normal"
                }
                btnSize = progress
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })


        ui.btnStartGame.setOnClickListener {
            val customization = Customization(
                repetition = repetition,
                isBtnRandom= ui.switchRandonButton.isChecked,
                isBtnIndicator = ui.switchNextBtn.isChecked,
                btnNumber = btnNumber,
                isFreeMode = isFreeMode,
                btnSize = btnSize
            )
            val i = Intent(this, PrescribedGameActivity::class.java)
            i.putExtra(CUSTOMIZATION_KEY, customization)
//            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)
            finish()
        }

        ui.goalCancle.setOnClickListener {
            finish()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
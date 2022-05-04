package au.edu.utas.kit721.strokerehab.ui.game

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import au.edu.utas.kit721.strokerehab.databinding.ActivityPrescribedGameBinding
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import au.edu.utas.kit721.strokerehab.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Collections.shuffle

class PrescribedGameActivity : AppCompatActivity() {
    private var round : Int = 0
    val targetBtns = mutableListOf<Int>()
    var randomBtns = mutableListOf<Int>()
    var currentBtn : Int = 1
    var btnPressedList = mutableListOf<Map<String, Int>>()
    var exerciseId = ""

    private lateinit var ui : ActivityPrescribedGameBinding

    //get db connection
    val db = Firebase.firestore
    var exercisesCollection = db.collection("exercises")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityPrescribedGameBinding.inflate(layoutInflater)
        setContentView(ui.root)

        val customization = intent.getSerializableExtra(CUSTOMIZATION_KEY) as Customization

        val repetition : Int = customization.repetition!!
        val isBtnRandom : Boolean = customization.isBtnRandom!!
        val isBtnIndicator : Boolean = customization.isBtnIndicator!!
        val numOfBtns : Int = customization.btnNumber!!
        val sizeOfBtn : Int = customization.btnSize!!
        val isFreeMode : Boolean = customization.isFreeMode!!


        exerciseId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")).toString()
        val exercise = Exercise(
            id = exerciseId,
            startAt = getCurrentDateTime(),
            repetition = repetition,
            completed = false,
        )

        exercisesCollection.document(exerciseId)
            .set(exercise)
            .addOnSuccessListener {
                Log.d(FIREBASE_TAG, "Document created with id ${exerciseId}")
            }
            .addOnFailureListener {
                Log.e(FIREBASE_TAG, "Error writing document", it)
            }

        if (isFreeMode) {
            ui.textViewGoal.text = "Free Mode"
            ui.textViewGoalNum.text = " "
            ui.textViewRoundLable.text = " "
        } else ui.textViewGoalNum.text = repetition.toString()
        ui.textViewRoundNum.text = round.toString()

        ui.btnInGame.setOnClickListener {
            endGame()
        }

//        add buttons into  target ordered button list
        for (number in 1..numOfBtns) {
            targetBtns.add(number)
        }
        randomBtns = targetBtns.toMutableList()


//        random buttons order
        if (isBtnRandom) shuffle(randomBtns)

        val linearLayout : LinearLayout = ui.btnLayout

        for(btnId in targetBtns) {
            // Create Button Dynamically
            val btnShow = Button(this)
            btnShow.text = randomBtns[btnId-1].toString()
            btnShow.id = btnId
            Log.d("currentBtn is:  ", currentBtn.toString())
            btnShow.setBackgroundResource(R.drawable.btn_round_primary)

            //if button indicator  is ON
            if (isBtnIndicator) {
                if (btnShow.text == currentBtn.toString()) btnShow.setBackgroundResource(R.drawable.btn_round_indicator)
            }


            btnShow.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)

            var param = btnShow.layoutParams as ViewGroup.MarginLayoutParams

            when (sizeOfBtn){
                0 -> {param.width = 100; param.height = 100}
                1 -> {param.width = 140; param.height = 140}
                3 -> {param.width = 180; param.height = 180}
                else -> {param.width = 180; param.height = 180}
            }

            // first time random position use margin
            if (isBtnRandom) param.setMargins((-400..400).random(),40,0,40) else param.setMargins(0,60,0,60)

            btnShow.setOnClickListener {
                var btnTime = getCurrentDateTime()
                var btnPressed = mapOf<String, Int>(
                    btnTime.toString() to randomBtns[btnId-1]
                )

                addBtnPressedToDB(btnPressed)


                if (randomBtns[btnId-1] == currentBtn){
                    // reset clicked button color
                    btnShow.setBackgroundResource(R.drawable.btn_round_primary)
                    btnShow.text = "\u2713"

                    currentBtn++
                    if (isBtnIndicator) nextBtnIndicator(currentBtn)

                    //  after done one round, reorder number and random position
                    if (currentBtn-1 == numOfBtns) {
                        currentBtn = 1
                        if (isBtnRandom) shuffle(randomBtns)
                        btnReText()
                        round++
                        ui.textViewRoundNum.text = round.toString()
                        if (isBtnIndicator) nextBtnIndicator(currentBtn)
                        if (isBtnRandom) param.setMargins((-400..400).random(),40,0,40)
                    }

                    // round completed
                    if (!isFreeMode) {
                        if (round == repetition) completeGame()
                    }
                }

            }

            // Add Button to LinearLayout
            if (linearLayout != null) {
                linearLayout.addView(btnShow)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        super.onDestroy()
        exercisesCollection.document(exerciseId)
            .update("endAt", getCurrentDateTime())
            .addOnSuccessListener { Log.d(FIREBASE_TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(FIREBASE_TAG, "Error updating document", e) }
    }

    private fun btnReText() {
        for(btnId in targetBtns) {
            var btn : Button = findViewById<Button>(btnId)
            btn.text = randomBtns[btnId-1].toString()
        }
    }

    fun nextBtnIndicator(id:Int) {
        for (btnId in targetBtns) {
            var btn: Button = findViewById<Button>(btnId)
            if (btn.text == id.toString()) btn.setBackgroundResource(R.drawable.btn_round_indicator)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun completeGame() {
        var time = getCurrentDateTime()
        exercisesCollection.document(exerciseId)
            .update("completed", true,"endAt",time)
            .addOnSuccessListener { Log.d(FIREBASE_TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(FIREBASE_TAG, "Error updating document", e) }
        val i = Intent(this, PrescribedGameDoneActivity::class.java)
        i.putExtra(EXERCISE_INDEX, exerciseId)
        startActivity(i)
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun endGame() {
        var time = getCurrentDateTime()
        exercisesCollection.document(exerciseId)
            .update("endAt",time,"repetition",round)
            .addOnSuccessListener { Log.d(FIREBASE_TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(FIREBASE_TAG, "Error updating document", e) }
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDateTime(): String? {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        return currentTime.format(formatter)
    }

    private fun addBtnPressedToDB (btnPressed : Map<String,Int>) {
        exercisesCollection.document(exerciseId)
            .update("btnPressed", FieldValue.arrayUnion(btnPressed))
            .addOnSuccessListener { Log.d(FIREBASE_TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(FIREBASE_TAG, "Error updating document", e) }
    }
}
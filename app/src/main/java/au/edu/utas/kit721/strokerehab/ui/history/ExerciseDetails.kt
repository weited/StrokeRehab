package au.edu.utas.kit721.strokerehab.ui.history

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.edu.utas.kit721.strokerehab.EXERCISE_INDEX
import au.edu.utas.kit721.strokerehab.FIREBASE_TAG
import au.edu.utas.kit721.strokerehab.databinding.ActivityExercisesDetailsBinding
import au.edu.utas.kit721.strokerehab.databinding.ButtonListItemBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ExerciseDetails : AppCompatActivity() {

    var currentPhotoPath : String? = null

    private lateinit var ui : ActivityExercisesDetailsBinding
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityExercisesDetailsBinding.inflate(layoutInflater)
        setContentView(ui.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Exercise details"
        actionbar.setDisplayHomeAsUpEnabled(true)

        val db = Firebase.firestore
        var exercisesCollection = db.collection("exercises")

        val exerciseID = intent.getIntExtra(EXERCISE_INDEX, -1)
        var exerciseObject = items[exerciseID]

        currentPhotoPath = exerciseObject.photoPath.toString()
        if (currentPhotoPath != null) {
//            setPic(ui.exerciseImageView)
        }


        ui.textDetailGoal.text = exerciseObject.repetition.toString()
        if (exerciseObject.completed == true) ui.textDetailStatus.text = "Completed" else ui.textDetailStatus.text = "Uncompleted"
        ui.textDetailStart.text = exerciseObject.startAt.toString()
        ui.textDetailEndAt.text = (if (exerciseObject.endAt != null) exerciseObject.endAt else "Game ended unexpectedly").toString()

        var buttonList = exerciseObject.btnPressed

        if (buttonList != null) {
            if (buttonList.size > 0) {
                ui.buttonList.adapter = ButtonAdapter(buttons = buttonList)
            }
        } else ui.textViewNoBtn.visibility = View.VISIBLE
        ui.buttonList.layoutManager = LinearLayoutManager(this)


        ui.detailShare.setOnClickListener {
            var sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, exerciseObject.toCsvFormat())
                type = "text/plain"
            }
            startActivity(Intent.createChooser(sendIntent, "Share via..."))
        }

        ui.detailDelete.setOnClickListener {
            exercisesCollection.document(exerciseObject.id!!)
                .delete()
                .addOnSuccessListener {
                    Log.d(FIREBASE_TAG, "Successfully deleted exercise ${exerciseObject?.id}")
                    //return to the list
                    finish()
                }
                .addOnFailureListener { e -> Log.w(FIREBASE_TAG, "Error deleting document", e) }
        }
    }

    inner class ButtonHolder(var ui: ButtonListItemBinding) : RecyclerView.ViewHolder(ui.root) {}
    inner class ButtonAdapter(private val buttons: MutableList<Map<String, Int>>?) : RecyclerView.Adapter<ButtonHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonHolder {
            val ui = ButtonListItemBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            return ButtonHolder(ui)
        }

        override fun onBindViewHolder(holder: ButtonHolder, position: Int) {
            val button = buttons!![position] //get the data at the requested position
            holder.ui.btnPressedTime.text = button.keys.toString().replace("[","").replace("]","")
            holder.ui.btnPressedNum.text = button.values.toString()
            ui.textViewNoBtn.text = "You have clicked ${itemCount} time${if (itemCount > 1) "s" else ""} button in this game!"
        }

        override fun getItemCount(): Int {
            return buttons?.size!!
        }
    }

    private fun setPic(imageView: ImageView) {
        // Get the dimensions of the View
        val targetW: Int = imageView.measuredWidth
        val targetH: Int = imageView.measuredHeight

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            BitmapFactory.decodeFile(currentPhotoPath, this)

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.max(1, Math.min(photoW / targetW, photoH / targetH))

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
            imageView.setImageBitmap(bitmap)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
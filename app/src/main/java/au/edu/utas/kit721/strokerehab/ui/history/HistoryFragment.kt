package au.edu.utas.kit721.strokerehab.ui.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.edu.utas.kit721.strokerehab.*
import au.edu.utas.kit721.strokerehab.databinding.FragmentHistoryBinding
import au.edu.utas.kit721.strokerehab.databinding.HistoryListItemBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

val items = mutableListOf<Exercise>()

class HistoryFragment : Fragment() {

    var sumBtnOfExercise = 0
    var sumOfExercise = 0
    var btnCountAry = mutableListOf<Int>()


    //get db connection
    val db = Firebase.firestore

    var exercisesCollection = db.collection("exercises")
    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val ui get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = ui.root

        fetchHistoryData()

        ui.textHistorySum.text = "Loading"
        ui.historyList.adapter = HistoryAdapter(exercises = items)
        ui.historyList.layoutManager = LinearLayoutManager(activity)

        return root
    }

    override fun onResume() {
        super.onResume()
//        (ui.historyList.adapter as HistoryAdapter).notifyDataSetChanged()
        fetchHistoryData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    inner class HistoryHolder(var ui: HistoryListItemBinding) : RecyclerView.ViewHolder(ui.root) {}
    inner class HistoryAdapter(private val exercises: MutableList<Exercise>) : RecyclerView.Adapter<HistoryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
            val ui = HistoryListItemBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            return HistoryHolder(ui)
        }

        override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
            val exercise = exercises[position]   //get the data at the requested position
            holder.ui.repetitionTimes.text = exercise.repetition.toString()
            holder.ui.historyStartAt.text = exercise.startAt
            holder.ui.historyEndAt.text = exercise.endAt

            holder.ui.root.setOnClickListener {
                var i = Intent(holder.ui.root.context, ExerciseDetails::class.java)
                i.putExtra(EXERCISE_INDEX, position)
                startActivity(i)
            }
        }

        override fun getItemCount(): Int {
            return exercises.size
        }
    }

    private fun fetchHistoryData() {
        exercisesCollection
            .get()
            .addOnSuccessListener { result ->
                items.clear() //this line clears the list, and prevents a bug where items would be duplicated upon rotation of screen
                Log.d(FIREBASE_TAG, "--- all exercises ---")
                for (document in result)
                {

                    val exercise = document.toObject<Exercise>()
                    exercise.id = document.id
                    Log.d("#############", exercise.toString())

                    items.add(exercise)
                }
                //after data fetched, count all buttons
                countAllBtnPressed()
                sumOfExercise = items.size
                ui.textHistorySum.text = "You have played ${sumOfExercise.toString()} exercise${if (sumOfExercise > 1) "s" else ""} and pressed ${sumBtnOfExercise.toString()} button${if (sumOfExercise > 1) "s" else ""} in the all time!"
                (ui.historyList.adapter as HistoryFragment.HistoryAdapter).notifyDataSetChanged()
            }
    }

    private fun countAllBtnPressed() {
        btnCountAry.clear()
        for (item in items) {
            if (item.btnPressed != null) {
                btnCountAry.add(item.btnPressed!!.size)
            }
        }
        sumBtnOfExercise = btnCountAry.sum()
    }
}
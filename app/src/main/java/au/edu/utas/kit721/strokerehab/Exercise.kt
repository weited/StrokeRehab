package au.edu.utas.kit721.strokerehab

class Exercise(var id : String? = null,
               var repetition : Int? = null,
               var completed : Boolean ? = null,
               var startAt : String? = null,
               var endAt : String? = null,
               var btnPressed: MutableList<Map<String,Int>>? = null,
               var photoPath : String? = null) {

    fun toCsvFormat(): String {
        var titleColumn =
            "Game title,Game mode,Repetitions,Completed,Start at,End at,Button pressed\n"
        var completedStr : String = if (completed != null) "Completed" else "Uncompleted"
        var endAtStr : String? = "Game ended unexpectedly"
        var buttonStr : String? =  "\"No button pressed in the game!\""

        if (btnPressed != null) {
                buttonStr = btnPressed.toString()
                .replace("[","")
                .replace("]","")
                .replace("="," pressed ")
        }

        var singleExercise =
            "${repetition.toString()}, ,${repetition.toString()},${completedStr},${startAt},${endAtStr},\"${buttonStr}\"\n"
        return "$titleColumn$singleExercise"
    }
}
package au.edu.utas.kit721.strokerehab

import java.io.Serializable

data class Customization (
    var repetition : Int? = 1,
    var isBtnRandom : Boolean? = true,
    var isBtnIndicator : Boolean? = true,
    var btnNumber : Int? = 3,
    var isFreeMode : Boolean? = null,
    var btnSize : Int? = 1,
) : Serializable

package developer.abdulaziz.registrationapp

import java.io.Serializable

data class User(
    var name: String? = null,
    var number: String? = null,
    var time: String? = null,
    var date: String? = null,
    var uid: String? = null
) : Serializable
package tech.siham.papp.models


data class InterestedIn (
    val url: String,
    val title: String,
    val description: String,
    val popular: Int,
    val usersIn: Int,
    val suggestedTimes: Int,
    val display: Boolean,
    val created: String,
    val imageUrl: String,
    val iconUrl: String
    //@Json(name='description') val desc: String
)
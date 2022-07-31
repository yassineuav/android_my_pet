package tech.siham.papp.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "bio")
    val bio: String?,
    @Json(name = "first_name")
    val firstName: String?,
    @Json(name = "username")
    val username: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "imageUrl")
    val imageUrl: String?,
    @Json(name = "last_name")
    val lastName: String?,
    @Json(name = "phone")
    val phone: String?
)
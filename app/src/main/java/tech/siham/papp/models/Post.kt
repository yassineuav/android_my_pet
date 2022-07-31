package tech.siham.papp.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Post(
    @Json(name = "content")
    val content: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "imageUrl")
    val imageUrl: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "user")
    val user: User?
)
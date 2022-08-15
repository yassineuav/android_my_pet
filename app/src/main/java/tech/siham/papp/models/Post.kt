package tech.siham.papp.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Post(
    @Json(name = "content_type")
    val contentType: String?,
    @Json(name = "create_at")
    val createAt: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "display")
    val display: Boolean?,
    @Json(name = "liked")
    val liked: Boolean?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "imageUrl")
    val imageUrl: String?,
    @Json(name = "likes_count")
    val likesCount: Int?,
    @Json(name = "uploaded_from")
    val uploadedFrom: String?,
    @Json(name = "user")
    val user: User?
)
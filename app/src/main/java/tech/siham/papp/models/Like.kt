package tech.siham.papp.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Like(
    @Json(name = "create_at")
    val createAt: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "liked")
    val liked: Boolean?,
    @Json(name = "post_id")
    val postId: Int?,
    @Json(name = "user_id")
    val userId: Int?
)
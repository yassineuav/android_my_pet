package tech.siham.papp.data.requests

import com.google.gson.annotations.SerializedName

data class PostRequest(

    @SerializedName("title")
    var title: String,

    @SerializedName("user_id")
    var userId: Int,

    @SerializedName("description")
    var description: String,

    @SerializedName("content")
    var content: String,

)

package tech.siham.papp.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import tech.siham.papp.data.requests.LoginRequest
import tech.siham.papp.data.requests.PostRequest
import tech.siham.papp.data.requests.RegisterRequest
import tech.siham.papp.models.*
import tech.siham.papp.papp.network.authInterceptor
import tech.siham.papp.utils.Constants
import tech.siham.papp.utils.Constants.BASE_URL
import tech.siham.papp.utils.Constants.DELETE_POST_URL
import tech.siham.papp.utils.Constants.INTERESTED_IN_URL
import tech.siham.papp.utils.Constants.MY_POST_URL
import tech.siham.papp.utils.Constants.POST_URL
import tech.siham.papp.utils.Constants.PROFILE_URL
import tech.siham.papp.utils.Constants.REGISTER_URL


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val okhttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor())
        .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(okhttpClient)
    .baseUrl(BASE_URL)
    .build()

interface ApiService {

    //@FormUrlEncoded
    @POST(Constants.LOGIN_URL)
    @Headers("Accept: application/json", "Content-Type: application/json")
    fun login(@Body request: LoginRequest): Deferred<Token>

    // @FormUrlEncoded
    @POST(REGISTER_URL)
    fun Register(@Body request: RegisterRequest): Deferred<String>

    @POST(REGISTER_URL)
    fun getRegister(@Body request: RegisterRequest): Call<String>

    @GET(PROFILE_URL)
    fun getUser(): Deferred<User>

    @GET(POST_URL)
    fun getPost(): Deferred<List<Post>>

    @PUT(POST_URL)
    fun editPost(@Body request: Post): Deferred<Post>

    @DELETE(DELETE_POST_URL)
    fun deletePost( @Path("id") id:Int? ): Deferred<String>

    @DELETE(DELETE_POST_URL)
    fun deleteAllPost(@Path("id") @Body request: Int): Deferred<Post>


    // CRUD my posts
    @GET(MY_POST_URL)
    fun getMyPosts(): Deferred<List<MyPost>>

    @Multipart
    @POST(MY_POST_URL)
    fun setPost(@Part() image: MultipartBody.Part,
                @Part("user_id") userId:Int,
                @Part("title") title:RequestBody,
                @Part("description") description:RequestBody,
                @Part("content") content:RequestBody
    ): Deferred<PostRequest>

    // @Part MultipartBody.Part image, @Part("name") RequestBody name
    // @Part("user_id") userId: RequestBody,


    @GET(INTERESTED_IN_URL)
    fun getInterestedIn(): Deferred<List<InterestedIn>>

    @POST(INTERESTED_IN_URL)
    fun setInterestedIn(@Body params: InterestedIn): Deferred<InterestedIn>

}

object ServiceAPI {
    val retrofitService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

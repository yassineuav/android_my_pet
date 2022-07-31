package tech.siham.papp.ui.mypost

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import java.io.File
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import tech.siham.papp.data.network.ServiceAPI
import tech.siham.papp.data.requests.PostRequest
import tech.siham.papp.models.MyPost
import tech.siham.papp.models.Post


enum class LoadingMyPostStatus { LOADING, ERROR, DONE, NO_ITEMS}

class MyPostViewModel : ViewModel() {

    private val _status = MutableLiveData<LoadingMyPostStatus>()
    val status: LiveData<LoadingMyPostStatus>
        get() = _status

    val isLoading = ObservableBoolean()

    fun onRefresh() {
        isLoading.set(true)
        getMyPosts()
    }

    private val _post = MutableLiveData<List<MyPost>>()
    val post : LiveData<List<MyPost>>
        get() = _post

    private val _details = MutableLiveData<String>()
    val details : LiveData<String>
        get() = _details

    private val _myPost = MutableLiveData<PostRequest>()
    val myPost : LiveData<PostRequest>
        get() = _myPost


    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getMyPosts()
    }


    private fun getMyPosts() {

        coroutineScope.launch {
            val getPostDeferred = ServiceAPI.retrofitService.getMyPosts()
            try{
                _status.value = LoadingMyPostStatus.LOADING
                val listResult = getPostDeferred.await()
                if(listResult.isNotEmpty()){
                    _post.value = listResult
                    _status.value = LoadingMyPostStatus.DONE
                }else {
                    _post.value = ArrayList()
                    _status.value = LoadingMyPostStatus.NO_ITEMS
                }
            }catch (t:Throwable){
                Log.e("fetch data:  Fail : ", t.message.toString())
                _status.value = LoadingMyPostStatus.ERROR
                _post.value = ArrayList()
            }finally {
                isLoading.set(false)
            }
        }

    }

     fun setPost(file: File, postRequest : PostRequest) {

        coroutineScope.launch {

            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("imageUrl", file.name, requestBody);

//            val title = postRequest.title.toRequestBody("text/plain".toMediaTypeOrNull())
//            val userId = postRequest.userId.toRequestBody("text/plain".toMediaTypeOrNull())
//            val desc = postRequest.description.toRequestBody("text/plain".toMediaTypeOrNull())
//            val content = postRequest.content.toRequestBody("text/plain".toMediaTypeOrNull())


            val setPostDeferred = ServiceAPI.retrofitService.setPost(
                filePart,
                postRequest.userId,
                postRequest.title,
                postRequest.description,
                postRequest.content)

            try{
                _status.value = LoadingMyPostStatus.LOADING
                val listResult = setPostDeferred.await()
                _myPost.value = listResult
                // getMyPosts()
                Log.e("upload data:success:", "data upload success")
            }catch (t:Throwable){
                Log.e("upload data:Fail:", t.message.toString() )
                _status.value = LoadingMyPostStatus.ERROR
            }finally {
                _status.value = LoadingMyPostStatus.DONE
                isLoading.set(false)
            }

        }

    }

    fun deleteAllPost(post: Post) {

        /*
        coroutineScope.launch {
            val setPostDeferred = ServiceAPI.retrofitService.setPost(post)
            try{
                _status.value = loadingStatus.LOADING
                val listResult = setPostDeferred.await()
                if(listResult != null){
                    _myPost.value = listResult
                }
                _status.value = loadingStatus.DONE

            }catch (t:Throwable){
                Log.i("fetch data:  Fail : ", t.message.toString())
                _status.value = loadingStatus.ERROR
                _myPost.value = Post("error: "+ t.message.toString(), "","","","")
            }finally {
                isLoading.set(false)
            }
        }

         */
    }


    fun deletePost(id:Int?) {
        coroutineScope.launch {
            val deletePostDeferred = ServiceAPI.retrofitService.deletePost(id)
            try{
                _status.value = LoadingMyPostStatus.LOADING
                val listResult = deletePostDeferred.await()
                if(listResult != null){
                    _details.value = listResult
                }
                Log.i("onDelete: ", listResult)

            }catch (t:Throwable){
                Log.i("fetch data:  Fail : ", t.message.toString())
                _status.value = LoadingMyPostStatus.ERROR
                _details.value = t.message.toString()
            }finally {
                getMyPosts()
                isLoading.set(false)
                _status.value = LoadingMyPostStatus.DONE
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}


package tech.siham.papp.ui.post

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import tech.siham.papp.data.network.ServiceAPI
import tech.siham.papp.models.Post


enum class loadingStatus { LOADING, ERROR, DONE, NO_ITEMS}

class PostViewModel : ViewModel() {

    private val _status = MutableLiveData<loadingStatus>()
    val status: LiveData<loadingStatus>
        get() = _status

    val isLoading = ObservableBoolean()

    fun onRefresh() {
        isLoading.set(true)
        getPost()
    }

    private val _post = MutableLiveData<List<Post>>()
    val post : LiveData<List<Post>>
        get() = _post

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getPost()
    }


    private fun getPost() {

        coroutineScope.launch {
            val getPostDeferred = ServiceAPI.retrofitService.getPost()
            try{
                _status.value = loadingStatus.LOADING
                val listResult = getPostDeferred.await()
                if(listResult.isNotEmpty()){
                    _post.value = listResult
                    _status.value = loadingStatus.DONE
                }else {
                    _post.value = ArrayList()
                    _status.value = loadingStatus.NO_ITEMS
                }

            }catch (t:Throwable){
                Log.i("fetch data:  Fail : ", t.message.toString())
                _status.value = loadingStatus.ERROR
                _post.value = ArrayList()
            }finally {
                isLoading.set(false)
            }
        }

    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
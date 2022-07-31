package tech.siham.papp.ui.profile

import androidx.databinding.ObservableBoolean
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import tech.siham.papp.data.network.ServiceAPI
import tech.siham.papp.models.User
import tech.siham.papp.utils.SessionManager


enum class loadingStatus { LOADING, ERROR, DONE}

class ProfileViewModel : ViewModel() {

    private val _status = MutableLiveData<loadingStatus>()
    val status: LiveData<loadingStatus>
        get() = _status

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val isLoading = ObservableBoolean()


    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

//    init {
//        getLogin()
//    }

    fun getUser(){
        coroutineScope.launch {

            val getUserDeferred = ServiceAPI.retrofitService.getUser()
            try{
                val result = getUserDeferred.await()
                _status.value = loadingStatus.LOADING
                _user.value = result
                result.id?.let { SessionManager().saveUserId(it) }

            }catch (t:Throwable){
                Log.i("fetch data:  Fail : ", t.message.toString())
                _status.value = loadingStatus.ERROR

            }finally {
                _status.value = loadingStatus.DONE
                isLoading.set(false)
            }


        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
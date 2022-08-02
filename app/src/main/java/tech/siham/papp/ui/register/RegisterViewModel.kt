package tech.siham.papp.ui.register

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
import tech.siham.papp.data.requests.RegisterRequest

enum class loadingStatus { LOADING, ERROR, DONE}

class RegisterViewModel : ViewModel() {

    private val _status = MutableLiveData<loadingStatus>()
    val status: LiveData<loadingStatus>
        get() = _status

    private val _details = MutableLiveData<RegisterRequest>()
    val details: LiveData<RegisterRequest>
        get() = _details


    private val isLoading = ObservableBoolean()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

//    init {
//        getLogin()
//    }

    fun setRegister(registerRequest: RegisterRequest){
        coroutineScope.launch {

            val getLoginDeferred = ServiceAPI.retrofitService.Register(registerRequest)
            try{
                _status.value = loadingStatus.LOADING
                val result = getLoginDeferred.await()
                _details.value = result
                _status.value = loadingStatus.DONE

            }catch (t:Throwable){
                Log.e("Register failed ", t.message.toString())
                _status.value = loadingStatus.ERROR
                _details.value = RegisterRequest("error:"+ t.message.toString(),"","")
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
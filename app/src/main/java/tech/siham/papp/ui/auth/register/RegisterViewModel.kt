package tech.siham.papp.ui.auth.register

import androidx.databinding.ObservableBoolean
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.*
import tech.siham.papp.data.network.ServiceAPI
import tech.siham.papp.data.requests.RegisterRequest

enum class RegisterStatus { LOADING, ERROR, DONE}

class RegisterViewModel : ViewModel() {

    private val _status = MutableLiveData<RegisterStatus>()
    val status: LiveData<RegisterStatus>
        get() = _status

    private val _details = MutableLiveData<String>()
    val details: LiveData<String>
        get() = _details


    private val isLoading = ObservableBoolean()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun setRegister(registerRequest: RegisterRequest) {
        coroutineScope.launch {
            val getLoginDeferred = ServiceAPI.retrofitService.Register(registerRequest)
            Log.e(
                "Register condition: ",
                registerRequest.email + " " + registerRequest.password + " " + registerRequest.username
            )

            try {

                _status.value = RegisterStatus.LOADING
                val result = getLoginDeferred.await()
                _details.value = result
                _status.value = RegisterStatus.DONE
                Log.e("Register details ", "msg: $result")
            } catch (e: HttpException) {
                val jsonObj = JSONObject(e.response()?.errorBody()!!.charStream().readText())
                val error = jsonObj.getJSONArray("username").getJSONObject(0).toString()

                Log.e(
                    "Register failed ",
                    "code: ${e.code()} \nresponse: ${
                        e.response()?.errorBody()!!.charStream().readText()
                    } \nmessage: ${e.message()}"
                )
                _status.value = RegisterStatus.ERROR
                _details.value = "error: $e"
            } finally {
                isLoading.set(false)
            }
        }
    }

    fun setRegisterCall(registerRequest: RegisterRequest) {

        val setRegisterUser = ServiceAPI.retrofitService.getRegister(registerRequest)
        Log.e(
            "Register condition: ",
            registerRequest.email + " " + registerRequest.password + " " + registerRequest.username
        )

        setRegisterUser.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.e("success Register details ", "msg: $response")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("fail Register details ", "msg: ${t.message}")
            }
        })
    }




    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
        /*
            try{
                _status.value = RegisterStatus.LOADING
                val result = setRegisterUser.await()
                _details.value = result
                _status.value = RegisterStatus.DONE
                Log.e("Register details ","msg: $result")
            }catch (e:HttpException){
                val jsonObj = JSONObject(e.response()?.errorBody()!!.charStream().readText())
                val error = jsonObj.getJSONArray("username").getJSONObject(0).toString()

                Log.e("Register failed ", "code: ${e.code()} \nresponse: ${e.response()?.errorBody()!!.charStream().readText()} \nmessage: ${e.message()}" )
                _status.value = RegisterStatus.ERROR
                _details.value = "error: $e"
            }finally {
                isLoading.set(false)
            }
            */





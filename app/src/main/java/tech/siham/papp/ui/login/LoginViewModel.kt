package tech.siham.papp.ui.login

import androidx.databinding.ObservableBoolean
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import tech.siham.papp.data.network.ServiceAPI
import tech.siham.papp.data.requests.LoginRequest
import tech.siham.papp.models.Token
import tech.siham.papp.utils.SessionManager


enum class loginStatus { LOADING, ERROR, DONE}

class LoginViewModel : ViewModel() {

    private val _status = MutableLiveData<loginStatus>()
    val status: LiveData<loginStatus>
        get() = _status

    private val _details = MutableLiveData<String>()
    val details: LiveData<String>
        get() = _details


    private val _user = MutableLiveData<String>()
    val user: LiveData<String>
        get() = _user

    private val isLoading = ObservableBoolean()

    private val _loginToken = MutableLiveData<Token>()
    val loginToken : LiveData<Token>
        get() = _loginToken

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

//    init {
//        getLogin()
//    }

    fun getLogin(loginRequest: LoginRequest){
        coroutineScope.launch {
            val getLoginDeferred = ServiceAPI.retrofitService.login(loginRequest)

            try {
                _status.value = loginStatus.LOADING

                val result = getLoginDeferred.await()


                if(result.access != null){
                    _loginToken.value = result
                    SessionManager().saveAuthToken(result.access)
                    _details.value = "user login successfully"
                }
                Log.i("fetch data:  success: ", result.toString())

            } catch (e: HttpException){
                if(e.code() == 401){
                    _details.value = "No active account found with the given credentials"
                }
                Log.i("fetch data:  Fail : ", e.toString() +"\n >> "+ e.message().toString())
                //_status.value = loginStatus.ERROR
                _loginToken.value = Token(access = "null", refresh = "null", detail = e.message.toString())
            } catch (t: Throwable){
                Log.i("fetch data:  Fail : ", t.toString() +"\n >> "+ t.message.toString())
                _status.value = loginStatus.ERROR
                _loginToken.value = Token(access = "null", refresh = "null", detail = t.message.toString())
            } finally {
                _status.value = loginStatus.DONE
                isLoading.set(false)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}
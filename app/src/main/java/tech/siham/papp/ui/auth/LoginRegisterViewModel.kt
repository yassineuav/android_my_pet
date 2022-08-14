package tech.siham.papp.ui.auth

import androidx.databinding.ObservableBoolean
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.*
import tech.siham.papp.data.network.ServiceAPI
import tech.siham.papp.data.requests.LoginRequest
import tech.siham.papp.data.requests.RegisterRequest
import tech.siham.papp.models.ErrorRegister
import tech.siham.papp.models.Token
import tech.siham.papp.utils.SessionManager

enum class RegisterStatus { LOADING, ERROR, DONE}
enum class LoginStatus { LOADING, ERROR, DONE}

class LoginRegisterViewModel : ViewModel() {

    private val _loginStatus = MutableLiveData<LoginStatus>()
    val loginStatus: LiveData<LoginStatus>
        get() = _loginStatus

    private val _loginDetails = MutableLiveData<String>()
    val loginDetails: LiveData<String>
        get() = _loginDetails


    private val _user = MutableLiveData<String>()
    val user: LiveData<String>
        get() = _user

    private val _loginToken = MutableLiveData<Token>()
    val loginToken : LiveData<Token>
        get() = _loginToken



    private val _registerStatus = MutableLiveData<RegisterStatus>()
    val registerStatus: LiveData<RegisterStatus>
        get() = _registerStatus

    private val _registerDetails = MutableLiveData<RegisterRequest>()
    val registerDetails: LiveData<RegisterRequest>
        get() = _registerDetails

    private val _registerErrorDetails = MutableLiveData<String>()
    val registerErrorDetails: LiveData<String>
        get() = _registerErrorDetails


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
                _registerStatus.value = RegisterStatus.LOADING
                val result = getLoginDeferred.await()
//                _registerDetails.value = result
                _registerStatus.value = RegisterStatus.DONE
                Log.e("Register details ", "msg: $result")
                getLogin(LoginRequest(registerRequest.username.toString(), registerRequest.password.toString()))


            } catch (e: HttpException) {
                val jsonObj = JSONObject(e.response()?.errorBody()!!.charStream().readText())
//                val errorUsername = jsonObj.getJSONArray("username").getJSONObject(0).toString()
//                val errorEmail = jsonObj.getJSONArray("email").getJSONObject(1).toString()
//                val errorBody = e.response()?.errorBody()!!.charStream().readText()

                val error = Gson().fromJson(jsonObj.toString(), ErrorRegister::class.java)

                val username = if(error.username != null) error.username.toString().drop(1).dropLast(1) else ""
                val email = if(error.email != null) error.email.toString().drop(1).dropLast(1)  else ""

                Log.e(
                    "Register failed ",
                    "code: ${e.code()} \n" +
                            "body: $error \n"+
                            "username: $username \n" +
                            "errorEmail: $email \n"
                )
//                _registerStatus.value = RegisterStatus.ERROR
                _registerErrorDetails.value = "$username\n$email"
            } finally {
                _registerStatus.value = RegisterStatus.DONE
                isLoading.set(false)
            }
        }
    }



    fun getLogin(loginRequest: LoginRequest){
        coroutineScope.launch {
            val getLoginDeferred = ServiceAPI.retrofitService.login(loginRequest)

            try {
                _loginStatus.value = LoginStatus.LOADING

                val result = getLoginDeferred.await()

                if(result.access != null){
                    _loginToken.value = result
                    SessionManager().saveAuthToken(result.access)
                    _loginDetails.value = "user login successfully"
                    // go to main activity
                }
                Log.i("fetch data:  success: ", result.toString())

            } catch (e: HttpException){
                if(e.code() == 401){
                    _loginDetails.value = "No active account found with the given credentials"
                }
                Log.i("fetch data:  Fail : ", e.toString() +"\n >> "+ e.message().toString())
                //_status.value = loginStatus.ERROR
                _loginToken.value = Token(access = "null", refresh = "null", detail = e.message.toString())
            } catch (t: Throwable){
                Log.i("fetch data:  Fail : ", t.toString() +"\n >> "+ t.message.toString())
                _loginStatus.value = LoginStatus.ERROR
                _loginToken.value = Token(access = "null", refresh = "null", detail = t.message.toString())
            } finally {
                _loginStatus.value = LoginStatus.DONE
                isLoading.set(false)
            }
        }
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
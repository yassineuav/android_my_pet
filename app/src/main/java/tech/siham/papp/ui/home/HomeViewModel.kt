package tech.siham.papp.ui.home

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
import tech.siham.papp.models.InterestedIn


enum class interestedApiStatus { LOADING, ERROR, DONE}

class HomeViewModel : ViewModel() {

    private val _status = MutableLiveData<interestedApiStatus>()
    val status: LiveData<interestedApiStatus>
        get() = _status

    val isLoading = ObservableBoolean()

    fun onRefresh() {
        isLoading.set(true)
        getInterestedIn()
    }

    private val _interestedIn = MutableLiveData<List<InterestedIn>>()
    val interestedIn : LiveData<List<InterestedIn>>
        get() = _interestedIn

    private val _navigateToSelectedInterestedIn = MutableLiveData<InterestedIn>()
    val navigateToSelectedInterestedIn : LiveData<InterestedIn>
        get() = _navigateToSelectedInterestedIn


    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getInterestedIn()
    }


    private fun getInterestedIn() {

        coroutineScope.launch {
            var getInterestedInDeferred = ServiceAPI.retrofitService.getInterestedIn()
            try{
                _status.value = interestedApiStatus.LOADING
                var listResult = getInterestedInDeferred.await()
                //_status.value = "Success: ${listResult.size} interested in list retrieved"
                if(listResult.isNotEmpty()){
                    _interestedIn.value = listResult
                }
                _status.value = interestedApiStatus.DONE

            }catch (t:Throwable){
                Log.i("fetch data:  Fail : ", t.message.toString())
                _status.value = interestedApiStatus.ERROR
                _interestedIn.value = ArrayList()
            }finally {
                isLoading.set(false)
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun displayInterestedInDetails(interestedIn: InterestedIn){
        _navigateToSelectedInterestedIn.value = interestedIn
    }

    fun displayInterestedInComplete(){
        _navigateToSelectedInterestedIn.value = null
    }

}
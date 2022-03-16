package ir.moonify.exchangeapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ir.moonify.exchangeapp.model.CurrencyHolder
import ir.moonify.exchangeapp.repository.MainRepository
import kotlinx.coroutines.*

class MainViewModel constructor(private val mainRepository: MainRepository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val currencyList = MutableLiveData<List<CurrencyHolder>>()
    var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    fun getAllCurrencies() {
        job = CoroutineScope(Dispatchers.IO).launch {
            loading.postValue(true)
            val currencyList = mainRepository.getAllCurrencies()
            withContext(Dispatchers.Main) {
                this@MainViewModel.currencyList.postValue(currencyList)
                loading.value = false
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}
package ir.moonify.exchangeapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ir.moonify.exchangeapp.ExchangeApplication
import ir.moonify.exchangeapp.R
import ir.moonify.exchangeapp.db.entities.Transaction
import ir.moonify.exchangeapp.model.CurrencyHolder
import ir.moonify.exchangeapp.repository.MainRepository
import ir.moonify.exchangeapp.util.Constants
import ir.moonify.exchangeapp.util.Utility
import kotlinx.coroutines.*
import java.text.DecimalFormat
import kotlin.reflect.full.memberProperties

class MainViewModel constructor(private val mainRepository: MainRepository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val successMessage = MutableLiveData<String>()
    val currencyList = MutableLiveData<List<CurrencyHolder>>()
    val transactionList = MutableLiveData<List<Transaction>>()
    val baseCurrency = MutableLiveData<CurrencyHolder>()
    val convertedValue = MutableLiveData<String>()
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

    fun getBaseCurrency() {
        job = CoroutineScope(Dispatchers.IO).launch {
            loading.postValue(true)
            val baseCurrency = mainRepository.getBaseCurrency()
            withContext(Dispatchers.Main) {
                this@MainViewModel.baseCurrency.postValue(baseCurrency)
                loading.value = false
            }
        }
    }

    fun getRates() {
        job = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val response = mainRepository.getRates(
                    ExchangeApplication.instance.applicationContext.getString(
                        R.string.access_key
                    )
                )
                mainRepository.saveExchangeData(response.body())
                delay(5000)
            }
        }
    }

    fun convert(sourceAmount: Double, sourceCurrency: String, destinationCurrency: String) {
        if (sourceCurrency == Constants.EUR) {
            var x =
                mainRepository.getExchangeData().rates!!::class.memberProperties.first { it.name == destinationCurrency }.getter.call(
                    mainRepository.getExchangeData().rates
                ).toString()

            convertedValue.value = Utility.convertDigits(sourceAmount.times(x.toDouble()))
        } else {
            convertedValue.value = "0"
        }
    }

    fun change(sourceAmount: Double, sourceCurrency: String, destinationCurrency: String) {
        var okMessage: String? = null
        var errorMessage: String? = null
        job = CoroutineScope(Dispatchers.IO).launch {
            var sourceBalance: Double = mainRepository.getCurrencyBalance(sourceCurrency)
            val amountPlusCommission: Double =
                sourceAmount + (sourceAmount.times(mainRepository.getCommission()))
            if (amountPlusCommission < sourceBalance) {
                var rate =
                    mainRepository.getExchangeData().rates!!::class.memberProperties.first { it.name == destinationCurrency }.getter.call(
                        mainRepository.getExchangeData().rates
                    )
                val destinationBalance =
                    mainRepository.getCurrencyBalance(destinationCurrency) + sourceAmount.times(
                        rate.toString().toDouble()
                    )
                mainRepository.writeNewBalances(
                    sourceCurrency,
                    sourceBalance - amountPlusCommission,
                    destinationCurrency, destinationBalance
                )
                okMessage = ExchangeApplication.instance.applicationContext.getString(
                    R.string.exchange_successfully,
                    sourceAmount.toString(),
                    sourceCurrency,
                    sourceAmount.times(
                        rate.toString().toDouble()
                    ).toString(),
                    destinationCurrency,
                    sourceAmount.times(mainRepository.getCommission()).toString(),
                    sourceCurrency
                )
                mainRepository.writeTransaction(
                    sourceCurrency,
                    destinationCurrency,
                    sourceAmount,
                    false
                )
                if (mainRepository.getCommission() > 0) {
                    mainRepository.writeTransaction(
                        sourceCurrency,
                        destinationCurrency,
                        sourceAmount.times(mainRepository.getCommission()),
                        true
                    )
                }
            } else {
                errorMessage =
                    ExchangeApplication.instance.applicationContext.getString(R.string.insufficient_balance)
            }
            withContext(Dispatchers.Main) {
                if (okMessage != null) {
                    successMessage.value = okMessage
                } else {
                    errorMessage?.let { onError(it) }
                }
            }
        }
    }

    fun getTransactions() {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = mainRepository.getTransactions()
            withContext(Dispatchers.Main) {
                transactionList.value = response
            }
        }
    }

    private fun onError(message: String) {
        successMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}
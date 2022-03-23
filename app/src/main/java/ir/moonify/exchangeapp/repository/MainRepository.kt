package ir.moonify.exchangeapp.repository

import androidx.room.Room
import androidx.room.RoomDatabase
import ir.moonify.exchangeapp.ExchangeApplication
import ir.moonify.exchangeapp.RetrofitService
import ir.moonify.exchangeapp.db.AppDatabase
import ir.moonify.exchangeapp.db.entities.Transaction
import ir.moonify.exchangeapp.model.CurrencyHolder
import ir.moonify.exchangeapp.model.ExchangeData
import ir.moonify.exchangeapp.util.Constants
import ir.moonify.exchangeapp.util.SharedPreferencesUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainRepository constructor(private val retrofitService: RetrofitService) {
    private val db = Room.databaseBuilder(
        ExchangeApplication.instance.applicationContext,
        AppDatabase::class.java, Constants.DATABASE_NAME
    ).build()

    fun getAllCurrencies(): List<CurrencyHolder> {
        val walletDAO = db.walletDao()
        return walletDAO.getAll()[0].currencies.sortedWith(compareByDescending { it.value })/*.filter { s -> s.name != Constants.EUR }*/
    }

    fun getBaseCurrency(): CurrencyHolder {
        val walletDAO = db.walletDao()
        return walletDAO.getAll()[0].currencies.sortedWith(compareByDescending { it.value })
            .single { s -> s.name == Constants.EUR }
    }

    fun saveExchangeData(rates: ExchangeData?) {
        SharedPreferencesUtility.instance.setExchangeData(rates)
    }

    fun getExchangeData(): ExchangeData {
        return SharedPreferencesUtility.instance.getExchangeData()
    }

    fun getCurrencyBalance(currency: String): Double {
        val walletDAO = db.walletDao()
        return walletDAO.getAll()[0].currencies.find { it.name == currency }!!.value
    }

    fun writeNewBalances(
        sourceCurrency: String,
        sourceValue: Double,
        destinationCurrency: String,
        destinationValue: Double
    ) {
        var walletDAO = db.walletDao()
        var wallet = walletDAO.getAll()[0]
        wallet.currencies.find { it.name == sourceCurrency }!!.value = sourceValue
        wallet.currencies.find { it.name == destinationCurrency }!!.value = destinationValue
        walletDAO.update(wallet)

    }

    fun writeTransaction(
        sourceCurrency: String,
        destinationCurrency: String,
        sourceAmount: Double,
        isCommission: Boolean
    ) {
        var transactionDao = db.transactionDao()
        transactionDao.insert(
            Transaction(
                sourceCurrency,
                destinationCurrency,
                sourceAmount,
                isCommission
            )
        )
    }

    fun getTransactions(): List<Transaction> {
        return db.transactionDao().getAll()
    }

    fun getCommission(): Double {
        return 0.007
    }

    suspend fun getRates(accessKey: String) = retrofitService.getExchangeData(accessKey)

}
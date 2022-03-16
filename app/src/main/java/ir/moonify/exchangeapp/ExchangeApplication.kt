package ir.moonify.exchangeapp

import android.app.Application
import android.content.SharedPreferences
import android.icu.util.Currency
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.gson.Gson
import ir.moonify.exchangeapp.db.AppDatabase
import ir.moonify.exchangeapp.db.dao.WalletDAO
import ir.moonify.exchangeapp.db.entities.Wallet
import ir.moonify.exchangeapp.model.CurrencyHolder
import ir.moonify.exchangeapp.model.OfflineRatesModel
import ir.moonify.exchangeapp.util.Constants
import ir.moonify.exchangeapp.util.SharedPreferencesUtility
import kotlinx.coroutines.*

class ExchangeApplication public constructor() : Application() {
    companion object {
        lateinit var instance: ExchangeApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (SharedPreferencesUtility.instance.isAppLoadedFirstTime()) {
            initDatabase()
        }
    }

    private fun initDatabase() {
        var currencyList: MutableList<CurrencyHolder> = mutableListOf()
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, Constants.DATABASE_NAME
        ).build()
        val gson: Gson = Gson()
        var offlineRates: OfflineRatesModel = OfflineRatesModel()
        offlineRates = gson.fromJson(Constants.OFFLINE_RATES, OfflineRatesModel::class.java)
        for (i in offlineRates.rates.indices) {
            val currencyHolder: CurrencyHolder = CurrencyHolder(
                offlineRates.rates.get(i),
                if (offlineRates.rates.get(i).equals(Constants.EUR)) 1000.00 else 0.0
            )
            currencyList.add(currencyHolder)
        }
        val walletDAO = db.walletDao()
        val wallet: Wallet = Wallet()
        wallet.currencies = currencyList
        var job: Job? = CoroutineScope(Dispatchers.IO).launch {
            walletDAO.insert(wallet)
            SharedPreferencesUtility.instance.setAppLoadedFirstTime(false)
        }
    }

}
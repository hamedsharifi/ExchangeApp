package ir.moonify.exchangeapp.repository

import androidx.room.Room
import androidx.room.RoomDatabase
import ir.moonify.exchangeapp.ExchangeApplication
import ir.moonify.exchangeapp.RetrofitService
import ir.moonify.exchangeapp.db.AppDatabase
import ir.moonify.exchangeapp.model.CurrencyHolder
import ir.moonify.exchangeapp.util.Constants
import kotlinx.coroutines.CoroutineScope

class MainRepository constructor(private val retrofitService: RetrofitService) {
    val db = Room.databaseBuilder(
        ExchangeApplication.instance.applicationContext,
        AppDatabase::class.java, Constants.DATABASE_NAME
    ).build()
    //    suspend fun getAllCurrencies() = retrofitService.getAllMovies()


    suspend fun getAllCurrencies(): List<CurrencyHolder> {
        val walletDAO = db.walletDao()
        return walletDAO.getAll().get(0).currencies
    }

}
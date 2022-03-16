package ir.moonify.exchangeapp.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ir.moonify.exchangeapp.model.CurrencyHolder
import java.lang.reflect.Type

class DataConverter {
    @TypeConverter
    fun fromCurrencyHolderList(currencies: List<CurrencyHolder?>?): String? {
        if (currencies == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<CurrencyHolder?>?>() {}.getType()
        return gson.toJson(currencies, type)
    }

    @TypeConverter
    fun toCurrencyHolderList(currencyHolderListString: String?): List<CurrencyHolder>? {
        if (currencyHolderListString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<CurrencyHolder?>?>() {}.getType()
        return gson.fromJson<List<CurrencyHolder>>(currencyHolderListString, type)
    }
}
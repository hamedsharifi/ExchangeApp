package ir.moonify.exchangeapp.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import ir.moonify.exchangeapp.ExchangeApplication
import ir.moonify.exchangeapp.model.ExchangeData

class SharedPreferencesUtility {
    private val mSharedPreferences: SharedPreferences =
        ExchangeApplication.instance.getSharedPreferences(
            Constants.APP_SHARED_PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )

    init {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
    }

    companion object {
        val instance = SharedPreferencesUtility()
    }

    fun setAppLoadedFirstTime(firstTime: Boolean) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putBoolean(Constants.APP_LOADED_FIRST_TIME, firstTime)
        editor.commit()
    }

    fun isAppLoadedFirstTime(): Boolean {
        return mSharedPreferences.getBoolean(Constants.APP_LOADED_FIRST_TIME, true)
    }

    fun setExchangeData(exchange: ExchangeData?) {
        val gson = Gson();
        val json: String = gson.toJson(exchange)
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putString(Constants.EXCHANGE_DATA, json)
        editor.apply()
    }

    fun getExchangeData(): ExchangeData {
        val gson = Gson()
        return gson.fromJson(
            mSharedPreferences.getString(Constants.EXCHANGE_DATA, ""),
            ExchangeData::class.java
        )
    }

}
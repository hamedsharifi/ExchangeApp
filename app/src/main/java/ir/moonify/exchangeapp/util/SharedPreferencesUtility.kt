package ir.moonify.exchangeapp.util

import android.content.Context
import android.content.SharedPreferences
import ir.moonify.exchangeapp.ExchangeApplication

class SharedPreferencesUtility {
    private val mSharedPreferences: SharedPreferences =
        ExchangeApplication.instance.getSharedPreferences(Constants.APP_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

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

}
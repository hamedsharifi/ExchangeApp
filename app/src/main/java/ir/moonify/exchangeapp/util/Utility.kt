package ir.moonify.exchangeapp.util

import java.text.DecimalFormat

object Utility {

    fun convertDigits(number: Double): String {
        val df = DecimalFormat("#.###")
        return df.format(number)
    }

}
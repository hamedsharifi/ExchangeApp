package ir.moonify.exchangeapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class ExchangeData (

    @SerializedName("success"   ) var success   : Boolean? = null,
    @SerializedName("timestamp" ) var timestamp : Int?     = null,
    @SerializedName("base"      ) var base      : String?  = null,
    @SerializedName("date"      ) var date      : String?  = null,
    @SerializedName("rates"     ) var rates     : RatesModel?   = RatesModel()

)
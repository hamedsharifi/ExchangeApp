package ir.moonify.exchangeapp.model

import com.google.gson.annotations.SerializedName

data class MainModel (

    @SerializedName("success"   ) var success   : Boolean? = null,
    @SerializedName("timestamp" ) var timestamp : Int?     = null,
    @SerializedName("base"      ) var base      : String?  = null,
    @SerializedName("date"      ) var date      : String?  = null,
    @SerializedName("rates"     ) var rates     : RatesModel?   = RatesModel()

)
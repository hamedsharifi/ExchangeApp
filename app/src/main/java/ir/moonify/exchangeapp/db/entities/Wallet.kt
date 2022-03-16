package ir.moonify.exchangeapp.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.moonify.exchangeapp.model.CurrencyHolder

@Entity
class Wallet {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "currencies")
    lateinit var currencies: List<CurrencyHolder>
}
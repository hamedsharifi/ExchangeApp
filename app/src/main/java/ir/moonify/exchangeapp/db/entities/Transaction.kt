package ir.moonify.exchangeapp.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Transaction {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    lateinit var source: String
    lateinit var destination: String
    var amount: Int = 0
    var commission: Int = 0
}
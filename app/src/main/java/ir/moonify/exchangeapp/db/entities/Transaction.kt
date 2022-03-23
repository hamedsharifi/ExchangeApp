package ir.moonify.exchangeapp.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
class Transaction {
    constructor(source: String, destination: String, amount: Double, isCommission: Boolean) {
        this.source = source
        this.destination = destination
        this.amount = amount
        this.isCommission = isCommission
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    lateinit var source: String
    lateinit var destination: String
    var amount: Double = 0.0
    var isCommission: Boolean = false
}
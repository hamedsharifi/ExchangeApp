package ir.moonify.exchangeapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ir.moonify.exchangeapp.db.entities.Transaction
import ir.moonify.exchangeapp.db.entities.Wallet

@Dao
interface TransactionsDAO {
    @Query("SELECT * FROM transactions")
    fun getAll(): List<Transaction>

    @Insert
    fun insert(myTransaction: Transaction)

}
package ir.moonify.exchangeapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ir.moonify.exchangeapp.db.entities.Wallet

@Dao
interface WalletDAO {
    @Query("SELECT * FROM wallet")
    fun getAll(): List<Wallet>

    @Insert
    fun insert(myWallet: Wallet)

    @Update
    fun update(myWallet: Wallet)

}
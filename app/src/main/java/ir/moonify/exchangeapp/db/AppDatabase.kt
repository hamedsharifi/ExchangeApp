package ir.moonify.exchangeapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ir.moonify.exchangeapp.db.dao.WalletDAO
import ir.moonify.exchangeapp.db.entities.Wallet

@TypeConverters(DataConverter::class)
@Database(entities = [Wallet::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun walletDao(): WalletDAO
}

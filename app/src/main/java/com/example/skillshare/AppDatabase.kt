package com.example.skillshare

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.skillshare.ui.ads.AdDao
import com.example.skillshare.ui.ads.AdEntity
import com.example.skillshare.ui.auth.User
import com.example.skillshare.ui.auth.UserDao
import com.example.skillshare.ui.exchange.Exchange
import com.example.skillshare.ui.exchange.ExchangeDao

@Database(
    entities = [AdEntity::class, User::class, Exchange::class],
    version = 18
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun adDao(): AdDao
    abstract fun userDao(): UserDao
    abstract fun exchangeDao(): ExchangeDao
    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "skillshare_db"
                )
                    .fallbackToDestructiveMigration() // чтобы не падала из-за версии
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
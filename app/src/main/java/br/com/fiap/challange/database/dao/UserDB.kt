package br.com.fiap.challange.database.dao

import br.com.fiap.challange.model.User
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class UserDB : RoomDatabase() {

    abstract fun userDao(): UserDAO

    companion object {

        private lateinit var instance: UserDB

        fun getDatabase(context: Context): UserDB {
            if (!Companion::instance.isInitialized) {
                instance = Room
                    .databaseBuilder(
                        context,
                        UserDB::class.java,
                        "user_db"
                    )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}
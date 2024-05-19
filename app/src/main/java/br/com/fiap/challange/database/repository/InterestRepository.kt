package br.com.fiap.challange.database.repository

import android.content.Context
import br.com.fiap.challange.database.dao.AppDatabase
import br.com.fiap.challange.model.Interest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InterestRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context).interestDao()

    suspend fun save(interest: Interest): Long {
        return withContext(Dispatchers.IO) {
            db.save(interest)
        }
    }

    fun update(interest: Interest): Int {
        return db.update(interest)
    }

    fun delete(interest: Interest): Int {
        return db.delete(interest)
    }

    fun getInterestById(id: Int): Interest {
        return db.getInterestById(id)
    }

    suspend fun getInterestByUserId(id: Long): List<Interest> {
        return withContext(Dispatchers.IO) {
            db.getInterestByUserId(id)
        }
    }

}
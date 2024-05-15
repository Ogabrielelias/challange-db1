package br.com.fiap.challange.database.repository

import android.content.Context
import br.com.fiap.challange.database.dao.AppDatabase
import br.com.fiap.challange.model.Interest

class InterestRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context).interestDao()

    fun save(interest: Interest): Long {
        return db.save(interest)
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
}
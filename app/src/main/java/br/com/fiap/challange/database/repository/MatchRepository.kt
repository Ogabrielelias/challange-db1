package br.com.fiap.challange.database.repository

import android.content.Context
import br.com.fiap.challange.database.dao.AppDatabase
import br.com.fiap.challange.model.Interest

class MatchRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context).matchDao()

    fun save(interest: Interest): Long {
        return db.save(interest)
    }

    fun update(interest: Interest): Int {
        return db.update(interest)
    }

    fun delete(interest: Interest): Int {
        return db.delete(interest)
    }
}
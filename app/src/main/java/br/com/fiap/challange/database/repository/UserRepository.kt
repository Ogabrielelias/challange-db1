package br.com.fiap.challange.database.repository

import android.content.Context
import br.com.fiap.challange.database.dao.AppDatabase
import br.com.fiap.challange.model.User

class UserRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context).userDao()

    fun save(user: User): Long {
        return db.save(user)
    }

    fun update(user: User): Int {
        return db.update(user)
    }

    fun delete(user: User): Int {
        return db.delete(user)
    }

    fun listUser(): List<User> {
        return db.listUser()
    }

    fun getUserById(id: Int): User {
        return db.getUserById(id)
    }

    fun getUserByLogin(email:String, senha:String): User {
        return db.getUserByLogin(email, senha)
    }

    fun getUserByEmail(email:String): User {
        return db.getUserByEmail(email)
    }

}
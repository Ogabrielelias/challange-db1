package br.com.fiap.challange.database.repository

import android.content.Context
import br.com.fiap.challange.database.dao.UserDB
import br.com.fiap.challange.model.User

class UserRepository(context: Context) {

    private val db = UserDB.getDatabase(context).userDao()

    fun save(contato: User): Long {
        return db.save(contato)
    }

    fun update(contato: User): Int {
        return db.update(contato)
    }

    fun delete(contato: User): Int {
        return db.delete(contato)
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

}
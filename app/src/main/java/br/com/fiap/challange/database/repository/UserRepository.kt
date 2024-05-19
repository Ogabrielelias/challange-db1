package br.com.fiap.challange.database.repository

import android.content.Context
import br.com.fiap.challange.database.dao.AppDatabase
import br.com.fiap.challange.model.User
import br.com.fiap.challange.model.UserWithExperiencesAndInterests
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context).userDao()

    suspend fun save(user: User): Long {
        return withContext(Dispatchers.IO) {
            db.save(user)
        }
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

    suspend fun getUserById(id: Long): User {
        return withContext(Dispatchers.IO) {
            db.getUserById(id)
        }
    }

    suspend fun searchUsers(
        searchTerm: String? = null,
        isMentor: Int? = null,
        isStudent: Int? = null,
        formationLevel: Int? = null,
        experienceLevel: Int? = null
    ): List<UserWithExperiencesAndInterests> {
        return withContext(Dispatchers.IO) {
            db.searchUsers(searchTerm, isMentor, isStudent, formationLevel, experienceLevel)
        }
    }

    suspend fun getUserByLogin(email: String, senha: String): User {
        return withContext(Dispatchers.IO) {
            db.getUserByLogin(email, senha)
        }
    }

    suspend fun getUserByEmail(email: String): User {
        return withContext(Dispatchers.IO) {
            db.getUserByEmail(email)
        }
    }

}
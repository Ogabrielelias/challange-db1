package br.com.fiap.challange.database.repository

import android.content.Context
import br.com.fiap.challange.database.dao.AppDatabase
import br.com.fiap.challange.model.Experience
import br.com.fiap.challange.model.Interest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExperienceRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context).experienceDao()

    suspend fun save(experience: Experience): Long {
        return withContext(Dispatchers.IO) {
            db.save(experience)
        }
    }

    fun update(experience: Experience): Int {
        return db.update(experience)
    }

    fun delete(experience: Experience): Int {
        return db.delete(experience)
    }

    fun getExperienceById(id: Int): Experience {
        return db.getExperienceById(id)
    }

    suspend fun getExperiencesByUserId(id: Long): List<Experience> {
        return withContext(Dispatchers.IO) {
            db.getExperiencesByUserId(id)
        }
    }
}
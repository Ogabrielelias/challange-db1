package br.com.fiap.challange.database.repository

import android.content.Context
import br.com.fiap.challange.database.dao.AppDatabase
import br.com.fiap.challange.model.Interest
import br.com.fiap.challange.model.Match
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MatchRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context).matchDao()

    suspend fun save(match: Match): Long {
        return withContext(Dispatchers.IO) {
            db.save(match)
        }
    }

    suspend fun update(match: Match): Int {
        return withContext(Dispatchers.IO) {
             db.update(match)
        }
    }

    fun delete(match: Match): Int {
        return db.delete(match)
    }

    suspend fun getMatchesByMentorStudentAndSubject(
        mentorId: Long,
        studentId: Long,
        matchSubject: String
    ): List<Match> {
        return withContext(Dispatchers.IO) {
            db.getMatchesByMentorStudentAndSubject(mentorId, studentId, matchSubject)
        }
    }
}
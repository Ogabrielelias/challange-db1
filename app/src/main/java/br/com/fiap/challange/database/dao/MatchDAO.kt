package br.com.fiap.challange.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.challange.model.Interest
import br.com.fiap.challange.model.Match

@Dao
interface MatchDAO {
    @Insert
    fun save(match: Match): Long

    @Update
    fun update(match: Match): Int

    @Delete
    fun delete(match: Match): Int

    @Query("""SELECT * FROM tb_match 
        WHERE userMentorId = :mentorId 
        AND userStudentId = :studentId 
        AND matchSubject = :matchSubject
        """)
    fun getMatchesByMentorStudentAndSubject(
        mentorId: Long,
        studentId: Long,
        matchSubject: String
    ): List<Match>
}
package br.com.fiap.challange.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.challange.model.User

@Dao
interface UserDAO {

    @Insert
    fun save(user: User): Long

    @Update
    fun update(user: User): Int

    @Delete
    fun delete(user: User): Int

    @Query("SELECT * FROM tb_user WHERE id = :id")
    fun getUserById(id: Int): User

    @Query("SELECT * FROM tb_user WHERE email = :email and password = :senha")
    fun getUserByLogin(email: String, senha: String): User

    @Query("SELECT * FROM tb_user WHERE email = :email ")
    fun getUserByEmail(email: String): User

    @Query("SELECT * FROM tb_user ORDER BY name ASC")
    fun listUser(): List<User>

    @Query(
        """
        SELECT DISTINCT u.*
        FROM tb_user u
        LEFT JOIN tb_experience e ON u.id = e.userId
        LEFT JOIN tb_interest i ON u.id = i.userId
        WHERE 
            (:searchTerm IS NULL OR u.name LIKE '%' || :searchTerm || '%' OR e.experience LIKE '%' || :searchTerm || '%' OR i.interest LIKE '%' || :searchTerm || '%')
            AND (:isMentor IS NULL OR u.isMentor = :isMentor)
            AND (:isStudent IS NULL OR u.isStudent = :isStudent)
            AND (:formation IS NULL OR e.level >= :formation)
            AND (:experienceLevel IS NULL OR i.level >= :experienceLevel);
    """
    )
    fun searchUsers(
        searchTerm: String?,
        isMentor: Boolean?,
        isStudent: Boolean?,
        formation: Int?,
        experienceLevel: Int?
    ): List<User>

    @Query("""
        SELECT DISTINCT u.*
            FROM tb_user u
            LEFT JOIN tb_interest i ON u.id = i.userId
            LEFT JOIN tb_experience e ON u.id = e.userId
            LEFT JOIN tb_match m ON u.id = m.userStudentId
            WHERE e.experience IN (:interestList)
              AND NOT (m.studentHasMatch = 1 AND m.mentorHasMatch = 1)
            ORDER BY 
              CASE 
                WHEN (i.interest IN (:interestList) AND m.studentHasMatch = 1) THEN 1
                WHEN i.interest IN (:interestList) THEN 2
                ELSE 3
              END
              
    """)
    fun getMentorToMatchFromInterests(interestList: List<String>): List<User>

    @Query("""
        SELECT DISTINCT u.*, i.*
        FROM tb_user u
        LEFT JOIN tb_experience e ON u.id = e.userId
        LEFT JOIN tb_interest i ON u.id = i.userId
        LEFT JOIN tb_match m ON u.id = m.userStudentId
        WHERE i.interest IN (:experienceList)
        AND (m.studentHasMatch IS NULL OR m.mentorHasMatch IS NULL)
        ORDER BY 
        CASE 
            WHEN e.experience IN (:experienceList) AND m.studentHasMatch = 1 THEN 1
            WHEN e.experience IN (:experienceList) THEN 2
            ELSE 3
        END;
    """)
    fun getStudentsToMatchFromExperiences(experienceList: List<String>): List<User>
}
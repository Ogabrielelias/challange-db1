package br.com.fiap.challange.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.challange.model.User
import br.com.fiap.challange.model.UserWithExperiencesAndInterests

@Dao
interface UserDAO {

    @Insert
    fun save(user: User): Long

    @Update
    fun update(user: User): Int

    @Delete
    fun delete(user: User): Int

    @Query("""
        SELECT u.*, e.experience AS user_experience, i.interest AS user_interest 
        FROM tb_user u
        LEFT JOIN tb_experience e ON u.id = e.userId
        LEFT JOIN tb_interest i ON u.id = i.userId
        WHERE u.id = :id
        """)
    fun getUserById(id: Long): UserWithExperiencesAndInterests

    @Query("""
        SELECT u.*, e.experience AS user_experience, i.interest AS user_interest 
        FROM tb_user u
        LEFT JOIN tb_experience e ON u.id = e.userId
        LEFT JOIN tb_interest i ON u.id = i.userId
        WHERE email = :email and password = :senha
    """)
    fun getUserByLogin(email: String, senha: String): UserWithExperiencesAndInterests

    @Query("SELECT * FROM tb_user WHERE email = :email ")
    fun getUserByEmail(email: String): User

    @Query("SELECT * FROM tb_user ORDER BY name ASC")
    fun listUser(): List<User>

    @Query(
        """
        SELECT DISTINCT u.*, e.experience AS user_experience, i.interest AS user_interest
        FROM tb_user u
        LEFT JOIN tb_experience e ON u.id = e.userId
        LEFT JOIN tb_interest i ON u.id = i.userId
        WHERE 
            (:searchTerm IS NULL OR u.name LIKE '%' || :searchTerm || '%' OR e.experience LIKE '%' || :searchTerm || '%' OR i.interest LIKE '%' || :searchTerm || '%')
            AND (:isMentor IS NULL OR (u.isMentor = :isMentor AND (:searchTerm IS NULL OR e.experience LIKE '%' || :searchTerm || '%')))
            AND (:isStudent IS NULL OR (u.isStudent = :isStudent AND (:searchTerm IS NULL OR i.interest LIKE '%' || :searchTerm || '%')))
            AND (:formationLevel IS NULL OR e.level >= :formationLevel)
            AND (:experienceLevel IS NULL OR i.level <= :experienceLevel);
    """
    )
    fun searchUsers(
        searchTerm: String?,
        isMentor: Int?,
        isStudent: Int?,
        formationLevel: Int?,
        experienceLevel: Int?
    ): List<UserWithExperiencesAndInterests>

    @Query("""
        SELECT DISTINCT u.*, e.experience AS user_experience, i.interest AS user_interest
        FROM tb_user u
        LEFT JOIN tb_experience e ON u.id = e.userId
        LEFT JOIN tb_interest i ON u.id = i.userId
        LEFT JOIN tb_match m ON u.id = m.userStudentId
        WHERE e.experience IN (:interestList)
        AND (m.studentHasMatch != 1 AND m.mentorHasMatch != 1)
        AND m.mentorHasMatch != 1
        ORDER BY 
        CASE 
            WHEN e.experience IN (:interestList) AND m.studentHasMatch = 1 AND m.userMentorId = :id THEN 1
            WHEN e.experience IN (:interestList) THEN 2
            ELSE 3
        END
              
    """)
    fun getMentorToMatchFromInterests(interestList: List<String>, id: Long): List<UserWithExperiencesAndInterests>

    @Query("""
        SELECT DISTINCT u.*, e.experience AS user_experience, i.interest AS user_interest
        FROM tb_user u
        LEFT JOIN tb_experience e ON u.id = e.userId
        LEFT JOIN tb_interest i ON u.id = i.userId
        LEFT JOIN tb_match m ON u.id = m.userStudentId
        WHERE i.interest IN (:experienceList)
        AND (m.studentHasMatch != 1 AND m.mentorHasMatch != 1)
        AND m.mentorHasMatch != 1
        ORDER BY 
        CASE 
            WHEN i.interest IN (:experienceList) AND m.studentHasMatch = 1 AND m.userMentorId = :id THEN 1
            WHEN i.interest IN (:experienceList) THEN 2
            ELSE 3
        END
    """)
    fun getStudentsToMatchFromExperiences(experienceList: List<String>, id:Long): List<UserWithExperiencesAndInterests>
}
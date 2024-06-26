package br.com.fiap.challange.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.challange.model.MentorToMatch
import br.com.fiap.challange.model.StudentToMatch
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
        SELECT DISTINCT u.id, u.name, u.email, u.password, u.age, u.genre, u.isMentor, u.isStudent, e.experience AS userExperience, e.level as experienceLevel, e.description as experienceDescription
        FROM tb_user u
        LEFT JOIN tb_experience e ON u.id = e.userId
        LEFT JOIN tb_interest i ON u.id = i.userId
        WHERE e.experience IN (:interestList)
        AND NOT EXISTS (
            SELECT 1
            FROM tb_match m
            WHERE m.userMentorId = u.id
            AND m.studentHasMatch IS NOT NULL
            AND m.userStudentId = :id
            AND e.experience = m.matchSubject
        )
        ORDER BY 
            CASE 
                WHEN e.experience IN (:interestList) THEN 2
                ELSE 3
            END
    """)
    fun getMentorToMatchFromInterests(interestList: List<String>, id: Long): List<MentorToMatch>

    @Query("""
        SELECT DISTINCT u.id, u.name, u.email, u.password, u.age, u.genre, u.isMentor, u.isStudent, i.interest AS userInterest, i.level as interestLevel, i.description as interestDescription
        FROM tb_user u
        LEFT JOIN tb_experience e ON u.id = e.userId
        LEFT JOIN tb_interest i ON u.id = i.userId
        WHERE i.interest IN (:experienceList)
        AND NOT EXISTS (
            SELECT 1
            FROM tb_match m
            WHERE m.userStudentId = u.id
            AND m.mentorHasMatch IS NOT NULL
            AND m.userMentorId = :id
            AND i.interest = m.matchSubject
        )
        ORDER BY 
            CASE 
                WHEN i.interest IN (:experienceList) THEN 2
                ELSE 3
            END
    """)
    fun getStudentsToMatchFromExperiences(experienceList: List<String>, id:Long): List<StudentToMatch>
}
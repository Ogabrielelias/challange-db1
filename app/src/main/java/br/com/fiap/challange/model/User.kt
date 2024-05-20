package br.com.fiap.challange.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "tb_user", indices = [Index(value = ["email"], unique = true)])
data class User (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val age: Int ,
    val genre: String = "",
    val isMentor: Int?,
    val isStudent: Int?
)

data class UserWithExperiencesAndInterests(
@Embedded val user: User,
@Relation(
    parentColumn = "id",
    entityColumn = "userId"
)
val experiences: List<Experience> = emptyList(),
@Relation(
    parentColumn = "id",
    entityColumn = "userId"
)
val interests: List<Interest> = emptyList()
)

data class MatchUserStudent(
    val id: Long = 0,
    val name: String = "",
    val subject: String = "",
    val level: Int,
    val description: String,
    val othersList: List<String>
)

data class StudentToMatch(
    val id: Long,
    val name: String,
    val email: String,
    val password: String,
    val age: Int,
    val genre: String,
    val isMentor: Int?,
    val isStudent: Int?,
    val userInterest: String,
    val interestLevel: Int,
    val interestDescription: String,
)

data class MentorToMatch(
    val id: Long,
    val name: String,
    val email: String,
    val password: String,
    val age: Int,
    val genre: String,
    val isMentor: Int?,
    val isStudent: Int?,
    val userExperience: String,
    val experienceLevel: Int,
    val experienceDescription: String,
)
package br.com.fiap.challange.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "tb_match",
    foreignKeys = [
        ForeignKey(entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userMentorId"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userStudentId"],
            onDelete = ForeignKey.CASCADE)
    ])
data class Match(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userMentorId: Long,
    val userStudentId: Long,
    val matchSubject: String,
    val mentorHasMatch: Int?,
    val studentHasMatch: Int?
)

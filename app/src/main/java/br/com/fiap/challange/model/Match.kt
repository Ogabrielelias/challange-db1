package br.com.fiap.challange.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_match")
data class Match(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userMentorId: Long?,
    val userStudentId: Long?,
    val mentorHasMatch: Boolean?,
    val studentHasMatch: Boolean?
)

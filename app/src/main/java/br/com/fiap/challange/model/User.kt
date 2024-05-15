package br.com.fiap.challange.model

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

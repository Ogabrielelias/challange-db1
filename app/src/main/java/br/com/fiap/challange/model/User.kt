package br.com.fiap.challange.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tb_user", indices = [Index(value = ["email"], unique = true)])
data class User (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val age: String = "",
    val genre: String = "",
    val isMentor: Boolean = false,
    val isStudent: Boolean = false
)

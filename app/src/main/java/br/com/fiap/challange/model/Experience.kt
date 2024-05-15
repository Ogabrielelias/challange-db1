package br.com.fiap.challange.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_experience")
data class Experience (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val experience: String,
    val level: Int,
    val description: String,
    val userId: Long
)
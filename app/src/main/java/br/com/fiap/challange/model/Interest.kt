package br.com.fiap.challange.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_interest")
data class Interest (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val interest: String,
    val level: String,
    val description: String,
    val userId: Long
)
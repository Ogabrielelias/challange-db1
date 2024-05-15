package br.com.fiap.challange.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "tb_interest",
    foreignKeys = [ForeignKey(entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE)])
data class Interest (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val interest: String,
    val level: Int,
    val description: String,
    val userId: Long
)
package br.com.fiap.challange.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "tb_notification",
    foreignKeys = [
        ForeignKey(entity = User::class,
            parentColumns = ["id"],
            childColumns = ["fromUserId"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = User::class,
            parentColumns = ["id"],
            childColumns = ["toUserId"],
            onDelete = ForeignKey.CASCADE)
    ])
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val hasSeen: Int?,
    val toUserId: Long,
    val fromUserId: Long,
    val message: String
)

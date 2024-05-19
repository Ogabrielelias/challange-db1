package br.com.fiap.challange.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tb_notification",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["fromUserId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["toUserId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val hasSeen: Int?,
    val hasReceived: Int?,
    val toUserId: Long,
    val requestType: String, //"mentor" ou "aluno"
    val commomSubject: String, // Matéria em comum
    val fromUserId: Long,
    val message: String
)

data class NotificationWithUserNames(
    val id: Long,
    val hasSeen: Int?,
    val hasReceived: Int?,
    val toUserId: Long,
    val fromUserId: Long,
    val requestType: String, //"mentor" ou "aluno"
    val commomSubject: String,
    val message: String,
    val fromUserName: String,
    val toUserName: String
)

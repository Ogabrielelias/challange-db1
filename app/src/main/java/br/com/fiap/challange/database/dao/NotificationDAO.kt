package br.com.fiap.challange.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.challange.model.Notification

@Dao
interface NotificationDAO {
    @Insert
    fun save(notification: Notification): Long

    @Update
    fun update(notification: Notification): Int

    @Delete
    fun delete(notification: Notification): Int

    @Query("SELECT * FROM tb_notification WHERE id = :userId")
    fun getNotificationsFromUserId (userId: Long): List<Notification>

    @Query("UPDATE tb_notification SET hasSeen = 1 WHERE toUserId = :userId AND id = :notificationId")
    fun markNotificationAsSeen(userId: Long, notificationId: Long): Int
}
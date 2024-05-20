package br.com.fiap.challange.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.challange.model.Notification
import br.com.fiap.challange.model.NotificationWithUserNames

@Dao
interface NotificationDAO {
    @Insert
    fun save(notification: Notification): Long

    @Update
    fun update(notification: Notification): Int

    @Delete
    fun delete(notification: Notification): Int

    @Query("""
            SELECT n.*, 
               u_from.name AS fromUserName, 
               u_to.name AS toUserName 
            FROM tb_notification n
            LEFT JOIN tb_user u_from ON n.fromUserId = u_from.id
            LEFT JOIN tb_user u_to ON n.toUserId = u_to.id
            WHERE toUserId = :userId 
            ORDER BY id DESC
            """)
    fun getNotificationsFromUserId (userId: Long): List<NotificationWithUserNames>

    @Query("""
        SELECT n.*, 
               u_from.name AS fromUserName, 
               u_to.name AS toUserName
        FROM tb_notification n
        LEFT JOIN tb_user u_from ON n.fromUserId = u_from.id
        LEFT JOIN tb_user u_to ON n.toUserId = u_to.id
        WHERE n.toUserId = :userId 
          AND n.hasSeen = 0 AND n.hasReceived = 0
        ORDER BY n.id DESC 
        LIMIT 1
        """)
    fun getLastNewNotificationsFromUserId (userId: Long): NotificationWithUserNames

    @Query("UPDATE tb_notification SET hasSeen = 1 WHERE id = :notificationId")
    fun markNotificationAsSeen(notificationId: Long): Int

    @Query("UPDATE tb_notification SET hasReceived = 1 WHERE id = :notificationId")
    fun markNotificationAsReceived(notificationId: Long): Int
}
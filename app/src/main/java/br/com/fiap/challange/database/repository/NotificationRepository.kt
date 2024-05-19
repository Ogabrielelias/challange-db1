package br.com.fiap.challange.database.repository

import android.content.Context
import br.com.fiap.challange.database.dao.AppDatabase
import br.com.fiap.challange.model.Notification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationRepository(context: Context) {
    private val db = AppDatabase.getDatabase(context).notificationDao()

    fun save(notification: Notification): Long {
        return db.save(notification)
    }

    fun update(notification: Notification): Int {
        return db.update(notification)
    }

    fun delete(notification: Notification): Int {
        return db.delete(notification)
    }

    suspend fun getNotificationsFromUserId(userId: Long): List<Notification> {
        return withContext(Dispatchers.IO) {
            db.getNotificationsFromUserId(userId)
        }
    }

    suspend fun markNotificationAsSeen(userId: Long, notificationId: Long): Int {
        return withContext(Dispatchers.IO) {
            db.markNotificationAsSeen(userId, notificationId)
        }
    }
}

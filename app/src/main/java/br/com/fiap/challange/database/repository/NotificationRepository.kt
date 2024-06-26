package br.com.fiap.challange.database.repository

import android.content.Context
import br.com.fiap.challange.database.dao.AppDatabase
import br.com.fiap.challange.model.Notification
import br.com.fiap.challange.model.NotificationWithUserNames
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationRepository(context: Context) {
    private val db = AppDatabase.getDatabase(context).notificationDao()

    suspend fun save(notification: Notification): Long {
        return withContext(Dispatchers.IO) {
            db.save (notification)
        }
    }

    fun update(notification: Notification): Int {
        return db.update(notification)
    }

    fun delete(notification: Notification): Int {
        return db.delete(notification)
    }

    suspend fun getNotificationsFromUserId(userId: Long): List<NotificationWithUserNames> {
        return withContext(Dispatchers.IO) {
            db.getNotificationsFromUserId(userId)
        }
    }

    suspend fun getLastNewNotificationsFromUserId(userId: Long): NotificationWithUserNames {
        return withContext(Dispatchers.IO) {
            db.getLastNewNotificationsFromUserId(userId)
        }
    }

    suspend fun markNotificationAsSeen(notificationId: Long): Int {
        return withContext(Dispatchers.IO) {
            db.markNotificationAsSeen( notificationId)
        }
    }

    suspend fun markNotificationAsReceived(notificationId: Long): Int {
        return withContext(Dispatchers.IO) {
            db.markNotificationAsReceived(notificationId)
        }
    }
}

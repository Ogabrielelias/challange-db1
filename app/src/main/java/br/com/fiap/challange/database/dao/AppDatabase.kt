package br.com.fiap.challange.database.dao
import br.com.fiap.challange.model.User
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.fiap.challange.model.Experience
import br.com.fiap.challange.model.Interest
import br.com.fiap.challange.model.Match
import br.com.fiap.challange.model.Notification

@Database(entities = [User::class, Interest::class, Experience::class, Match::class, Notification::class], version = 3, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDAO
    abstract fun interestDao(): InterestDAO
    abstract fun experienceDao(): ExperienceDAO
    abstract fun matchDao(): MatchDAO
    abstract fun notificationDao(): NotificationDAO

    companion object {

        private lateinit var instance: AppDatabase

        fun getDatabase(context: Context): AppDatabase {
                if (!Companion::instance.isInitialized) {
                    instance = Room
                        .databaseBuilder(
                            context,
                            AppDatabase::class.java,
                            "challange_db"
                        )
                        .createFromAsset("database/database_seeder.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            return instance
        }
    }
}
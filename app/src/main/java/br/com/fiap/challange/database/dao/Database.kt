    package br.com.fiap.challange.database.dao

    import br.com.fiap.challange.model.User
    import android.content.Context
    import androidx.room.Database
    import androidx.room.Room
    import androidx.room.RoomDatabase
    import br.com.fiap.challange.model.Interest

    @Database(entities = [User::class, Interest::class], version = 2)
    abstract class AppDatabase  : RoomDatabase() {

        abstract fun userDao(): UserDAO
        abstract fun interestDao(): InterestDAO

        companion object {

            private lateinit var instance: AppDatabase

            fun getDatabase(context: Context): AppDatabase {
                if (!Companion::instance.isInitialized) {
                    instance = Room
                        .databaseBuilder(
                            context,
                            AppDatabase ::class.java,
                            "challange_db"
                        )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }
    }
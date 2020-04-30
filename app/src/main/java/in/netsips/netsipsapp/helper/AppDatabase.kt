package `in`.netsips.netsipsapp.helper

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.Executors

@Database(entities = arrayOf(Article::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun articlesDao(): ArticlesDAO

    companion object {
        private var INSTANCE: AppDatabase? = null

        val databaseWriteExecutor = Executors.newSingleThreadExecutor()

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "all_articles"
                    ).build()
                }
            }
            return INSTANCE
        }
    }

}
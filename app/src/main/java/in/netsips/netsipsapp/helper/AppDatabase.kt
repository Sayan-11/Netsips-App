package `in`.netsips.netsipsapp.helper

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Article::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): ArticlesDAO
}
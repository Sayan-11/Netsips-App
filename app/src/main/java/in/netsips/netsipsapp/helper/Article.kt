package `in`.netsips.netsipsapp.helper

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_articles")
data class Article(
    @PrimaryKey val articleURL: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "faviconURL") val faviconUrl: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "imageURL") val imageUrl: String?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "dateAdded") val dateAdded: Long,
    @ColumnInfo(name = "isCurrentSession") val currentSession: Long
) {

    companion object {
        const val NOT_CURRENT_SESSION = 0L
        const val IS_CURRENT_SESSION = 1L
    }

}
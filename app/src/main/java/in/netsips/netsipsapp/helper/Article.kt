package `in`.netsips.netsipsapp.helper

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_session")
data class Article(
    @PrimaryKey val articleURL: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "faviconURL") val faviconUrl: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "imageURL") val imageUrl: String?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "dateAdded") val dateAdded: Long
)
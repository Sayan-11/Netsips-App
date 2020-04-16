package `in`.netsips.netsipsapp.helper

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_session")
data class Article(
    @PrimaryKey val articleURL: String,
    @ColumnInfo(name = "featured_image_url") val featuredImageURL: String,
    @ColumnInfo(name = "date_saved") val date: Long,
    @ColumnInfo(name = "title") val title: String
)
package `in`.netsips.netsipsapp.helper

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ArticlesDAO {

    @Query("SELECT * FROM all_articles ORDER BY dateAdded DESC")
    fun getAllArticles(): List<Article>

    @Query("SELECT * FROM all_articles WHERE isCurrentSession = 1 ORDER BY dateAdded DESC")
    fun getCurrentSessionArticles(): List<Article>

    @Query("SELECT * FROM all_articles WHERE isCurrentSession = 0 ORDER BY dateAdded DESC")
    fun getArchivedArticles(): List<Article>

    @Insert
    fun insertArticle(article: Article)

}
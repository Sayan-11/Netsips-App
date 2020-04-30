package `in`.netsips.netsipsapp.helper

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ArticlesDAO {

    @Query("SELECT * FROM all_articles WHERE isCurrentSession = 1 ORDER BY dateAdded DESC")
    fun getCurrentSessionArticles(): LiveData<List<Article>>

    @Query("SELECT * FROM all_articles WHERE isCurrentSession = 0 ORDER BY dateAdded DESC")
    fun getArchivedArticles(): LiveData<List<Article>>

    @Insert
    fun insertArticle(article: Article)

}
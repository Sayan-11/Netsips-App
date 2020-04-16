package `in`.netsips.netsipsapp.helper

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ArticlesDAO {

    @Query("SELECT * FROM current_session")
    fun getAllArticles(): List<Article>

    @Insert
    fun insertArticle(article: Article)

}
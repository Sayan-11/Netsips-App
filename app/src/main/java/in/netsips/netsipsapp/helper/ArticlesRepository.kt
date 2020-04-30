package `in`.netsips.netsipsapp.helper

import android.app.Application


class ArticlesRepository(application: Application) {

    private val articlesDAO = AppDatabase.getInstance(application)?.articlesDao()
    val currentSessionArticles = articlesDAO?.getCurrentSessionArticles()
    val archivedArticles = articlesDAO?.getArchivedArticles()

    fun insert(article: Article) {
        AppDatabase.databaseWriteExecutor.execute {
            articlesDAO?.insertArticle(article)
        }
    }

}
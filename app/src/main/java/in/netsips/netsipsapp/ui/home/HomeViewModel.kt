package `in`.netsips.netsipsapp.ui.home

import `in`.netsips.netsipsapp.helper.ArticlesRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val articlesRepository = ArticlesRepository(application)
    val currentSessionArticles = articlesRepository.currentSessionArticles
}
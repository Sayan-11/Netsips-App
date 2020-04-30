package `in`.netsips.netsipsapp.ui.archive

import `in`.netsips.netsipsapp.helper.ArticlesRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class ArchiveViewModel(application: Application) : AndroidViewModel(application) {
    private val articlesRepository = ArticlesRepository(application)
    val currentSessionArticles = articlesRepository.archivedArticles
}
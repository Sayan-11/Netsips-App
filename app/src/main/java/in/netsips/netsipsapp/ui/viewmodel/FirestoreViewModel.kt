package `in`.netsips.netsipsapp.ui.viewmodel

import `in`.netsips.netsipsapp.R
import `in`.netsips.netsipsapp.helper.ArticlesRepository
import `in`.netsips.netsipsapp.helper.FirestoreArticle
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.regex.Pattern

class FirestoreViewModel(private val application: Application) : ViewModel() {

    private val TAG = "FirestoreViewModel"
    private val articlesRepository = ArticlesRepository(application)
    var allArticles: MutableLiveData<List<FirestoreArticle>> = MutableLiveData()
    var currentArticles: MutableLiveData<List<FirestoreArticle>> = MutableLiveData()
//trash vale articles
    fun getArchivedArticles(): LiveData<List<FirestoreArticle>> {
        articlesRepository.getAllArticles().whereIn(
            application.getString(R.string.firestore_user_collection_field_article_status),
            listOf(
                FirestoreArticle.DELETED
            )
        )
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.e(TAG, "Listening failed")
                    allArticles.value = null
                    return@addSnapshotListener
                }

                val allArticlesList: MutableList<FirestoreArticle> = mutableListOf()
                for (doc in value!!) {
                    val article = FirestoreArticle(
                        doc.id,
                        doc.getString(application.getString(R.string.firestore_user_collection_field_article_url))!!,
                        doc.getString(application.getString(R.string.firestore_user_collection_field_article_image_url))!!,
                        doc.getString(application.getString(R.string.firestore_user_collection_field_article_title))!!,
                        doc.getString(application.getString(R.string.firestore_user_collection_field_article_tags))!!,
                        doc.getDate(application.getString(R.string.firestore_user_collection_field_date_added))!!,
                        doc.getLong(application.getString(R.string.firestore_user_collection_field_article_status))!!
                    )
                    allArticlesList.add(article)
                }
                allArticlesList.sortByDescending { it.dateAdded }

                allArticles.value = allArticlesList
            }
        return allArticles
    }
private lateinit var savedArticlesList: MutableList<FirestoreArticle>
    fun getSavedArticles(): LiveData<List<FirestoreArticle>> {
        articlesRepository.getAllArticles().whereIn(
            application.getString(R.string.firestore_user_collection_field_article_status),
            listOf(
            FirestoreArticle.NEWSLETTER_SENT_ARCHIVED,
            FirestoreArticle.NEWSLETTER_NOT_SENT_ARCHIVED,
            FirestoreArticle.CURRENT_NOT_SENT_SAVED)
        )
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.e(TAG, "Listening failed")
                    currentArticles.value = null
                    return@addSnapshotListener
                }

                savedArticlesList = mutableListOf()
                for (doc in value!!) {
                    val article = FirestoreArticle(
                        doc.id,
                        doc.getString(application.getString(R.string.firestore_user_collection_field_article_url))!!,
                        doc.getString(application.getString(R.string.firestore_user_collection_field_article_image_url))!!,
                        doc.getString(application.getString(R.string.firestore_user_collection_field_article_title))!!,
                        doc.getString(application.getString(R.string.firestore_user_collection_field_article_tags))!!,
                        doc.getDate(application.getString(R.string.firestore_user_collection_field_date_added))!!,
                        doc.getLong(application.getString(R.string.firestore_user_collection_field_article_status))!!
                    )
                    savedArticlesList.add(article)
                }
                addTags(savedArticlesList)
                savedArticlesList.sortByDescending { it.dateAdded }
                currentArticles.value = savedArticlesList
            }
        return currentArticles
    }
    //_allTags was immutable so no chance of data change
    //MutableLiveData ke jaagah u sent LiveData non mutable
//    val _allTags: LiveData<MutableList<String>>
//        get() = allTags// whenever u call _all it actually gets the allTag value converts to LiveData and sends
    var allTags: MutableLiveData<MutableList<String>> = MutableLiveData()
     private fun addTags(articles: MutableList<FirestoreArticle>) {
        val tagsList: MutableList<String> = mutableListOf()
        articles.forEach { article ->
            val p = Pattern.compile("\\s*([\\w\\s]*|\\d*)\\s*(,|\$)").matcher(article.tags)
            while (p.find()) {
                val match = p.group(1) ?: "" //null hua to ""
                if(match.isNotEmpty()&&!tagsList.contains(match))
                    tagsList.add(match)
            }
        }
        allTags.value = tagsList
    }

    fun searchResult(tag: String): MutableList<FirestoreArticle>{
        val tagsList: MutableList<FirestoreArticle> = mutableListOf()
        savedArticlesList.forEach{
            if(it.tags.contains(tag))
                tagsList.add(it)
        }
        return tagsList
    }
    fun deleteArticle(docID: String) {
        articlesRepository.deleteArticle(docID)
        //removed Task<Void> return type from DeleteArticle fun to incorporate delete from trash in firestore(Status code 102 deletes from firestore)

//            .addOnSuccessListener {
//                Log.d(TAG, "Article deleted from database")
//            }
//            .addOnFailureListener {
//                Log.e(TAG, "Error deleting article from database: ${it.message}")
//            }
    }
    fun restoreArticle(docID: String){
        articlesRepository.restoreArticle(docID)
    }


}

class FirestoreViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FirestoreViewModel(
            application
        ) as T
    }
}
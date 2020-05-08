package `in`.netsips.netsipsapp.ui

import `in`.netsips.netsipsapp.R
import `in`.netsips.netsipsapp.helper.ArticlesRepository
import `in`.netsips.netsipsapp.helper.FirestoreArticle
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Query

class FirestoreViewModel(private val application: Application) : ViewModel() {

    private val TAG = "FirestoreViewModel"
    private val articlesRepository = ArticlesRepository(application)
    var allArticles: MutableLiveData<List<FirestoreArticle>> = MutableLiveData()
    var currentArticles: MutableLiveData<List<FirestoreArticle>> = MutableLiveData()

    fun getAllArticles(): LiveData<List<FirestoreArticle>> {
        articlesRepository.getAllArticles().orderBy(
            application.getString(R.string.firestore_user_collection_field_date_added),
            Query.Direction.DESCENDING
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
                allArticles.value = allArticlesList
            }
        return allArticles
    }

    fun getCurrentArticles(): LiveData<List<FirestoreArticle>> {
        articlesRepository.getAllArticles().whereEqualTo("status", FirestoreArticle.CURRENT_SESSION)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.e(TAG, "Listening failed")
                    currentArticles.value = null
                    return@addSnapshotListener
                }

                val savedArticlesList: MutableList<FirestoreArticle> = mutableListOf()
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
                savedArticlesList.sortByDescending { it.dateAdded }
                currentArticles.value = savedArticlesList
            }
        return currentArticles
    }

    fun deleteArticle(docID: String) {
        articlesRepository.deleteArticle(docID)
            .addOnSuccessListener {
                Log.d(TAG, "Article deleted from database")
            }
            .addOnFailureListener {
                Log.e(TAG, "Error deleting article from database: ${it.message}")
            }
    }

}
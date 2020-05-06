package `in`.netsips.netsipsapp.ui

import `in`.netsips.netsipsapp.helper.ArticlesRepository
import `in`.netsips.netsipsapp.helper.FirestoreArticle
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.Query

class FirestoreViewModel(application: Application) : AndroidViewModel(application) {

    val TAG = "FirestoreViewModel"
    private val articlesRepository = ArticlesRepository(application)
    var allArticles: MutableLiveData<List<FirestoreArticle>> = MutableLiveData()
    var currentArticles: MutableLiveData<List<FirestoreArticle>> = MutableLiveData()

    fun getAllArticles(): LiveData<List<FirestoreArticle>> {
        articlesRepository.getAllArticles().orderBy("dateAdded", Query.Direction.DESCENDING)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.e(TAG, "Listening failed")
                    allArticles.value = null
                    return@addSnapshotListener
                }

                var allArticlesList: MutableList<FirestoreArticle> = mutableListOf()
                for (doc in value!!) {
                    var article = FirestoreArticle(
                        doc.id,
                        doc.getString("articleURL")!!,
                        doc.getString("imageUrl")!!,
                        doc.getString("title")!!,
                        doc.getString("tags")!!,
                        doc.getLong("dateAdded")!!,
                        doc.getLong("status")!!
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

                var savedArticlesList: MutableList<FirestoreArticle> = mutableListOf()
                for (doc in value!!) {
                    var article = FirestoreArticle(
                        doc.id,
                        doc.getString("articleURL")!!,
                        doc.getString("imageUrl")!!,
                        doc.getString("title")!!,
                        doc.getString("tags")!!,
                        doc.getLong("dateAdded")!!,
                        doc.getLong("status")!!
                    )
                    savedArticlesList.add(article)
                }
                savedArticlesList.sortByDescending { it.dateAdded }
                currentArticles.value = savedArticlesList
            }
        return currentArticles
    }

    fun deleteArticle(article: FirestoreArticle) {
        articlesRepository.deleteArticle(article)
            .addOnSuccessListener {
                Log.d(TAG, "Article deleted from database")
            }
            .addOnFailureListener {
                Log.e(TAG, "Error deleting article from database: ${it.message}")
            }
    }

}
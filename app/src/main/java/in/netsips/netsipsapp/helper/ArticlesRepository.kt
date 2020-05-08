package `in`.netsips.netsipsapp.helper

import `in`.netsips.netsipsapp.R
import android.app.Application
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class ArticlesRepository(private val application: Application) {

    val TAG = "ArticlesRepository"
    private val firestoreDb = FirebaseFirestore.getInstance()
    private val uid: String = FirebaseAuth.getInstance().uid!!

    fun insertArticle(article: Article): Task<DocumentReference> {
        return firestoreDb.collection(uid).add(article)
    }

    fun getAllArticles(): CollectionReference {
        return firestoreDb.collection(uid)
    }

    fun deleteArticle(docId: String): Task<Void> {
        return firestoreDb.collection(uid).document(docId).update(
            application.getString(R.string.firestore_user_collection_field_article_status),
            FirestoreArticle.DELETED
        )
    }

}
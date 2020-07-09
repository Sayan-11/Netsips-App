package `in`.netsips.netsipsapp.helper

import `in`.netsips.netsipsapp.R
import android.app.Application
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class ArticlesRepository(private val application: Application) {

    val TAG = "ArticlesRepository"
    private val firestoreDb = FirebaseFirestore.getInstance()
    private val uid: String = FirebaseAuth.getInstance().currentUser!!.uid

    fun insertArticle(article: Article): Task<DocumentReference> {
        return firestoreDb.collection(uid).add(article)
    }

    fun getAllArticles(): CollectionReference {
        return firestoreDb.collection(uid)
    }

    fun deleteArticle(docId: String){

        firestoreDb.document("$uid/$docId").get()
            .addOnSuccessListener {
                val status=it.getLong(application.getString(R.string.firestore_user_collection_field_article_status))
                if(status != FirestoreArticle.DELETED) {
                    firestoreDb.collection(uid).document(docId).update(
                        application.getString(R.string.firestore_user_collection_field_article_status),
                        FirestoreArticle.DELETED
                    )
                }else{
                    //removes Article from Database if deleted from trash
                    firestoreDb.collection(uid).document(docId).delete()
                }
            }
    }
    fun restoreArticle(docId:String){
        firestoreDb.collection(uid).document(docId).update(
            application.getString(R.string.firestore_user_collection_field_article_status),
            FirestoreArticle.CURRENT_NOT_SENT_SAVED)

        firestoreDb.collection(uid).document(docId).update(
            application.getString(R.string.firestore_user_collection_field_date_added),
            Date()
        )
    }


}
package `in`.netsips.netsipsapp.helper

import java.util.*
//send data to firebase
data class Article(
    val articleURL: String,
    val imageUrl: String,
    val title: String,
    val tags: String,
    val dateAdded: Date,
    val status: Long
)

data class FirestoreArticle(
    val docID: String,
    val articleURL: String,
    val imageUrl: String,
    val title: String,
    val tags: String,
    val dateAdded: Date,
    val status: Long
) {
    companion object {
        const val CURRENT_NOT_SENT_SAVED = 100L
        const val NEWSLETTER_SENT_ARCHIVED = 101L
        const val DELETED = 102L
        const val NEWSLETTER_NOT_SENT_ARCHIVED = 103L
    }
}
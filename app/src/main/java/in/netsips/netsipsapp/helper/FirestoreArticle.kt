package `in`.netsips.netsipsapp.helper

data class Article(
    val articleURL: String,
    val imageUrl: String,
    val title: String,
    val tags: String,
    val dateAdded: Long,
    val status: Long
)

data class FirestoreArticle(
    val docID: String,
    val articleURL: String,
    val imageUrl: String,
    val title: String,
    val tags: String,
    val dateAdded: Long,
    val status: Long
) {
    companion object {
        const val CURRENT_SESSION = 100L
        const val NEWSLETTER_SENT = 101L
    }
}
package `in`.netsips.netsipsapp.helper

import `in`.netsips.netsipsapp.R
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*


class ArticleAdapter(private val articlesList: ArrayList<Article>) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_article, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(articlesList[position])
    }

    override fun getItemCount(): Int {
        return articlesList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(article: Article) {
            val articleDateText = itemView.findViewById<TextView>(R.id.article_date_text)
            val articleFeatureImage = itemView.findViewById<ImageView>(R.id.article_featured_image)
            val articleTitleText = itemView.findViewById<TextView>(R.id.article_title_text)
            articleDateText.text = formatDate(article.date)
            //TODO: Use picasso to set image
            articleTitleText.text = article.title

            itemView.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.articleURL))
                it.context.startActivity(browserIntent)
            }
        }

        private fun formatDate(dateToFormat: Date): String {
            return SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(dateToFormat)
        }

    }

}
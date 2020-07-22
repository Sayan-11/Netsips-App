package `in`.netsips.netsipsapp.helper

import `in`.netsips.netsipsapp.R
import `in`.netsips.netsipsapp.ui.AddTagBottomSheet
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


class ArticleAdapter(private val articlesList: List<FirestoreArticle>) :
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
//        companion object{
//            var tagsList= mutableListOf<String>()
//        }
        fun bindItems(article: FirestoreArticle) {
            val articleDateText = itemView.findViewById<TextView>(R.id.article_date_text)
            val articleFeatureImage = itemView.findViewById<ImageView>(R.id.roundedImageView)
            val articleTitleText = itemView.findViewById<TextView>(R.id.article_title_text)
            val link = itemView.findViewById<TextView>(R.id.link)
            val articleTagsText = itemView.findViewById<TextView>(R.id.article_tags_text)
            articleDateText.text = formatDate(article.dateAdded.time)
            if (article.imageUrl.isNotEmpty()) {
                if (article.imageUrl.contains(".png"))
                    Picasso.with(itemView.context).load(article.imageUrl).resize(1000, 1000)
                        .centerCrop()
                        .placeholder(R.drawable.ic_baseline_image_24).into(articleFeatureImage)
                else {
                    articleFeatureImage.setBackgroundResource(R.color.white)
                    articleFeatureImage.scaleType = ImageView.ScaleType.FIT_XY
                    Glide.with(itemView.context).load(article.imageUrl)
                        .apply(RequestOptions().override(1000, 1000).centerCrop())
                        .placeholder(R.drawable.ic_baseline_image_24).into(articleFeatureImage)
                }
                articleFeatureImage.setBackgroundResource(R.color.white)
                articleFeatureImage.scaleType = ImageView.ScaleType.FIT_XY
            }
            else{
                articleFeatureImage.setBackgroundResource(R.color.white)
                articleFeatureImage.scaleType=ImageView.ScaleType.FIT_XY
                articleFeatureImage.setImageResource(R.drawable.ic_baseline_image_24)}

            articleTitleText.text = article.title
            //Log.e("url",article.articleURL)
            val p = Pattern.compile(
                "(www\\.)?([a-zA-Z0-9-]+)(\\.[a-zA-Z]+)"
            ).matcher(article.articleURL)
            if(p.find()){
                link.text = (p.group(2))?.substring(0,1)?.toUpperCase(Locale.ROOT)
                    .plus(p.group(2)?.substring(1))
            }

            if (article.tags.isBlank())
                articleTagsText.text =itemView.context.getString(R.string.add_tag)
            else {
                articleTagsText.text =article.tags
            }

            articleTagsText.setOnClickListener {
                val activity = itemView.context as AppCompatActivity
                AddTagBottomSheet(article).show(activity.supportFragmentManager, article.docID)
            }

            itemView.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.articleURL))
                it.context.startActivity(browserIntent)
            }
        }

        private fun formatDate(dateToFormat: Long): String {
            return SimpleDateFormat("dd MMMM", Locale.getDefault()).format(Date(dateToFormat))
        }

        
    }

}
package `in`.netsips.netsipsapp

import `in`.netsips.netsipsapp.databinding.ActivityReceiveArticleBinding
import android.content.Intent
import android.os.Bundle
import android.webkit.URLUtil
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

class ReceiveArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityReceiveArticleBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_receive_article)

        val intentText = intent.getStringExtra(Intent.EXTRA_TEXT)

        if (URLUtil.isValidUrl(intentText)) {
            binding.articleUrlText.text = intentText
            binding.saveArticleFab.setOnClickListener {
                //TODO: Add article to database
                Toast.makeText(
                    applicationContext,
                    getString(R.string.article_added),
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        } else {
            TODO("URL is not valid. Write handler logic.")
        }

    }
}

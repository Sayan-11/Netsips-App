package `in`.netsips.netsipsapp

import `in`.netsips.netsipsapp.databinding.ActivityReceiveArticleBinding
import `in`.netsips.netsipsapp.helper.*
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class ReceiveArticleActivity : AppCompatActivity() {

    private lateinit var articlesRepository: ArticlesRepository

    private lateinit var binding: ActivityReceiveArticleBinding

    private lateinit var retrofitClient: Retrofit
    private lateinit var retrofitService: APIInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser != null) {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_receive_article)

            articlesRepository = ArticlesRepository(application)

            retrofitClient = Retrofit.Builder().baseUrl(getString(R.string.metadata_api_base_url))
                .addConverterFactory(GsonConverterFactory.create()).build()
            retrofitService = retrofitClient.create(APIInterface::class.java)

            val intentText = intent.getStringExtra(Intent.EXTRA_TEXT)

            val p = Pattern.compile(
                "((https?):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)",
                Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL
            ).matcher(intentText)
            if (p.find()) {
                val url = intentText.substring(p.start(0), p.end(0))
                binding.articleUrlText.text = url
                setArticleData(url)
            } else {
                Log.d("ReceiveArticleActivity", "Intent text: $intentText")
                Log.d("ReceiveArticleActivity", "URL unusable")
                Snackbar.make(
                    binding.receiveArticleCoordinator,
                    "URL is not valid",
                    Snackbar.LENGTH_INDEFINITE
                ).show()
                binding.articleLoadingProgress.visibility = View.GONE
            }
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun setArticleData(url: String) {
        val call = retrofitService.getMetaData(url)
        call.enqueue(object : Callback<MetaResult> {
            override fun onFailure(call: Call<MetaResult>, t: Throwable) {
                Snackbar.make(
                    binding.receiveArticleCoordinator,
                    "Unable to connect to the internet",
                    Snackbar.LENGTH_INDEFINITE
                ).show()
                binding.articleLoadingProgress.visibility = View.GONE
                Log.d("ReceiveArticleActivity", "Status: ${t.message}")
                t.printStackTrace()
            }

            override fun onResponse(call: Call<MetaResult>, response: Response<MetaResult>) {
                if (response.body()?.result?.status.equals("OK")) {
                    setNotLoading()
                    binding.articleDateText.text = formatDate(Date().time)
                    Picasso.with(applicationContext).load(response.body()?.meta?.image)
                        .into(binding.articleFeaturedImage)
                    binding.articleTitleText.text = response.body()?.meta?.title

                    binding.saveArticleFab.setOnClickListener {
                        articlesRepository.insertArticle(
                            Article(
                                url,
                                response.body()?.meta?.image ?: "",
                                response.body()?.meta?.title!!,
                                "",
                                Date(),
                                FirestoreArticle.CURRENT_NOT_SENT_SAVED
                            )
                        )
                            .addOnSuccessListener {
                                Log.d("ReceiveArticleActivity", "Article saved to Firestore!")
                            }
                            .addOnFailureListener {
                                Log.e(
                                    "ReceiveArticleActivity",
                                    "Error saving article to database: ${it.message}"
                                )
                            }
                        finish()
                    }
                } else {
                    binding.articleLoadingProgress.visibility = View.GONE
                    Snackbar.make(
                        binding.receiveArticleCoordinator,
                        "Unable to fetch article information",
                        Snackbar.LENGTH_INDEFINITE
                    ).show()
                }
            }

        })
    }

    private fun setNotLoading() {
        binding.articleLoadingProgress.visibility = View.GONE
        binding.articleDetailsCard.visibility = View.VISIBLE
        binding.saveArticleFab.visibility = View.VISIBLE
        binding.saveArticleFab.show()
    }

    private fun formatDate(dateToFormat: Long): String {
        return SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date(dateToFormat))
    }

}

//                Log.d("ReceiveArticleActivity", "Status: ${response.body()?.result?.status}")
//                Log.d("ReceiveArticleActivity", "Code: ${response.body()?.result?.code}")
//                Log.d("ReceiveArticleActivity", "Reason: ${response.body()?.result?.reason}")
//
//                Log.d("ReceiveArticleActivity", "Name: ${response.body()?.meta?.site?.name}")
//                Log.d("ReceiveArticleActivity", "Favicon: ${response.body()?.meta?.site?.favicon}")
//
//                Log.d("ReceiveArticleActivity", "Description: ${response.body()?.meta?.description}")
//                Log.d("ReceiveArticleActivity", "Image: ${response.body()?.meta?.image}")
//                Log.d("ReceiveArticleActivity", "Title: ${response.body()?.meta?.title}")


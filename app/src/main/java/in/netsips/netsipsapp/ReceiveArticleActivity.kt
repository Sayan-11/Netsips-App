package `in`.netsips.netsipsapp

import `in`.netsips.netsipsapp.databinding.ActivityReceiveArticleBinding
import `in`.netsips.netsipsapp.helper.APIInterface
import `in`.netsips.netsipsapp.helper.AppDatabase
import `in`.netsips.netsipsapp.helper.Article
import `in`.netsips.netsipsapp.helper.MetaResult
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.room.Room
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class ReceiveArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityReceiveArticleBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_receive_article)

        val db =
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "current_session")
                .allowMainThreadQueries().build()

        val retrofitClient = Retrofit.Builder().baseUrl(getString(R.string.metadata_api_base_url))
            .addConverterFactory(GsonConverterFactory.create()).build()
        val retrofitService = retrofitClient.create(APIInterface::class.java)

        val intentText = intent.getStringExtra(Intent.EXTRA_TEXT)
        binding.articleUrlText.text = intentText

        val call = retrofitService.getMetaData(intentText)
        call.enqueue(object : Callback<MetaResult> {
            override fun onFailure(call: Call<MetaResult>, t: Throwable) {
                Log.d("ReceiveArticleActivity", "Status: ${t.message}")
                t.printStackTrace()
            }

            override fun onResponse(call: Call<MetaResult>, response: Response<MetaResult>) {
                if (response.body()?.result?.status.equals("OK")) {
                    binding.articleDateText.text = formatDate(Date().time)
                    Picasso.with(applicationContext).load(response.body()?.meta?.image)
                        .into(binding.articleFeaturedImage)
                    binding.articleTitleText.text = response.body()?.meta?.title

                    binding.saveArticleFab.setOnClickListener {
                        db.userDao().insertArticle(
                            Article(
                                intentText, response.body()?.meta?.site?.name,
                                response.body()?.meta?.site?.favicon,
                                response.body()?.meta?.description,
                                response.body()?.meta?.image,
                                response.body()?.meta?.title,
                                Date().time
                            )
                        )
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.article_added),
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Failed to fetch article information: ${response.body()?.result?.reason}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })
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


package `in`.netsips.netsipsapp.ui

import `in`.netsips.netsipsapp.R
import `in`.netsips.netsipsapp.databinding.FragmentHomeBinding
import `in`.netsips.netsipsapp.helper.Article
import `in`.netsips.netsipsapp.helper.ArticleAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val articlesList = arrayListOf<Article>(
            Article(1, "", Date(), "Test Article 1", "https:www.bing.com"),
            Article(2, "", Date(), "Test Article 2", "https:www.bing.com"),
            Article(3, "", Date(), "Test Article 3", "https:www.bing.com")
        )

        binding.currentSessionRecycler.layoutManager = LinearLayoutManager(context)
        binding.currentSessionRecycler.adapter = ArticleAdapter(articlesList)

        return binding.root
    }

}
